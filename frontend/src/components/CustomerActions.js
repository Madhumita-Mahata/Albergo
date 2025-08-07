import { 
  getUserById,
  updateUser,
  getBookingsByUserId,
  getReviewByUserId,
  giveReview,
  makePayment
} from "../api/customer";

// Customer action configurations and logic
export const customerActions = [
  {
    id: "getProfile",
    label: "View Profile",
    icon: "👤",
    color: "bg-blue-500 hover:bg-blue-600",
    description: "View your profile information",
    action: async (data, userId) => {
      const result = await getUserById(userId);
      return [result];
    }
  },
  {
    id: "updateProfile",
    label: "Update Profile",
    icon: "✏️",
    color: "bg-green-500 hover:bg-green-600",
    description: "Update your profile information",
    hasInput: true,
    inputFields: [
      { name: "firstName", type: "text", placeholder: "First Name" },
      { name: "lastName", type: "text", placeholder: "Last Name" },
      { name: "email", type: "email", placeholder: "Email Address" },
      { name: "phone", type: "tel", placeholder: "Phone Number" }
    ],
    action: async (data, userId) => {
      // Remove empty fields
      const updateData = Object.fromEntries(
        Object.entries(data).filter(([key, value]) => value && value.trim() !== "")
      );
      
      if (Object.keys(updateData).length === 0) {
        throw new Error("Please provide at least one field to update");
      }
      
      const result = await updateUser(userId, updateData);
      return [{ message: result.message || "Profile updated successfully!" }];
    }
  },
  {
    id: "getBookings",
    label: "My Bookings",
    icon: "🏨",
    color: "bg-purple-500 hover:bg-purple-600",
    description: "View all your bookings",
    action: async (data, userId) => {
      const result = await getBookingsByUserId(userId);
      return Array.isArray(result) ? result : result.bookings || [];
    }
  },
  {
    id: "getReviews",
    label: "My Reviews",
    icon: "⭐",
    color: "bg-yellow-500 hover:bg-yellow-600",
    description: "View all your reviews",
    action: async (data, userId) => {
      const result = await getReviewByUserId(userId);
      return Array.isArray(result) ? result : result.reviews || [];
    }
  },
  {
    id: "giveReview",
    label: "Write Review",
    icon: "📝",
    color: "bg-indigo-500 hover:bg-indigo-600",
    description: "Write a review for your stay",
    hasInput: true,
    inputFields: [
      { 
        name: "rating", 
        type: "select", 
        placeholder: "Select Rating",
        options: ["1", "2", "3", "4", "5"]
      },
      { name: "comment", type: "textarea", placeholder: "Write your review..." }
    ],
    action: async (data, userId) => {
      const requiredFields = ["rating", "comment"];
      const missingFields = requiredFields.filter(field => !data[field]);
      
      if (missingFields.length > 0) {
        throw new Error(`Please fill in all required fields: ${missingFields.join(", ")}`);
      }
      
      const result = await giveReview(userId, {
        rating: parseInt(data.rating),
        comment: data.comment
      });
      return [{ message: result.message || "Review submitted successfully!" }];
    }
  },
  {
    id: "makePayment",
    label: "Make Payment",
    icon: "💳",
    color: "bg-red-500 hover:bg-red-600",
    description: "Make payment for a booking",
    hasInput: true,
    inputFields: [
      { name: "bookingId", type: "text", placeholder: "Booking ID" },
      { name: "amount", type: "number", placeholder: "Payment Amount" },
      { name: "paymentMethod", type: "select", placeholder: "Payment Method", options: ["CARD", "CASH", "UPI"] }
    ],
    action: async (data, userId) => {
      const requiredFields = ["bookingId", "amount", "paymentMethod"];
      const missingFields = requiredFields.filter(field => !data[field]);
      
      if (missingFields.length > 0) {
        throw new Error(`Please fill in all required fields: ${missingFields.join(", ")}`);
      }
      
      const result = await makePayment(data.bookingId, {
        amount: parseFloat(data.amount),
        paymentMethod: data.paymentMethod
      });
      return [{ message: result.message || "Payment processed successfully!" }];
    }
  }
];

// Execute a customer action by ID
export const executeCustomerAction = async (actionId, formData = {}, userId) => {
  const action = customerActions.find(a => a.id === actionId);
  if (!action) {
    throw new Error(`Action ${actionId} not found`);
  }
  
  return await action.action(formData, userId);
};
