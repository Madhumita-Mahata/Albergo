import { 
  getAllRooms,
  getRoomById,
  getRoomByRoomNumber,
  getRoomByCategory,
  addRoom,
  updateRoom,
  deleteRoom,
  getAllReviews,
  getReviewByUserId
} from "../api/manager";

// Manager action configurations and logic
export const managerActions = [
  {
    id: "getAllRooms",
    label: "All Rooms",
    icon: "🏨",
    color: "bg-blue-500 hover:bg-blue-600",
    description: "View all rooms in the hotel",
    action: async () => {
      const result = await getAllRooms();
      return Array.isArray(result) ? result : result.rooms || [];
    }
  },
  {
    id: "getRoomById",
    label: "Find Room by ID",
    icon: "🔍",
    color: "bg-green-500 hover:bg-green-600",
    description: "Search for a room by ID",
    hasInput: true,
    inputFields: [{ name: "roomId", type: "text", placeholder: "Enter Room ID" }],
    action: async (data) => {
      if (!data.roomId) {
        throw new Error("Please enter a Room ID");
      }
      const result = await getRoomById(data.roomId);
      return [result];
    }
  },
  {
    id: "getRoomByNumber",
    label: "Find Room by Number",
    icon: "🔢",
    color: "bg-purple-500 hover:bg-purple-600",
    description: "Search for a room by room number",
    hasInput: true,
    inputFields: [{ name: "roomNumber", type: "text", placeholder: "Enter Room Number" }],
    action: async (data) => {
      if (!data.roomNumber) {
        throw new Error("Please enter a Room Number");
      }
      const result = await getRoomByRoomNumber(data.roomNumber);
      return [result];
    }
  },
  {
    id: "getRoomByCategory",
    label: "Rooms by Category",
    icon: "📋",
    color: "bg-yellow-500 hover:bg-yellow-600",
    description: "Filter rooms by category",
    hasInput: true,
    inputFields: [{ 
      name: "category", 
      type: "select", 
      placeholder: "Select Category",
      options: ["STANDARD", "DELUXE", "SUITE", "PREMIUM"]
    }],
    action: async (data) => {
      if (!data.category) {
        throw new Error("Please select a category");
      }
      const result = await getRoomByCategory(data.category);
      return Array.isArray(result) ? result : [result];
    }
  },
  {
    id: "addRoom",
    label: "Add New Room",
    icon: "➕",
    color: "bg-indigo-500 hover:bg-indigo-600",
    description: "Add a new room to the hotel",
    hasInput: true,
    inputFields: [
      { name: "roomNumber", type: "text", placeholder: "Room Number" },
      { 
        name: "category", 
        type: "select", 
        placeholder: "Select Category",
        options: ["STANDARD", "DELUXE", "SUITE", "PREMIUM"]
      },
      { name: "price", type: "number", placeholder: "Price per night" },
      { 
        name: "status", 
        type: "select", 
        placeholder: "Room Status",
        options: ["AVAILABLE", "OCCUPIED", "MAINTENANCE"]
      },
      { name: "description", type: "textarea", placeholder: "Room description (optional)" }
    ],
    action: async (data) => {
      const requiredFields = ["roomNumber", "category", "price", "status"];
      const missingFields = requiredFields.filter(field => !data[field]);
      
      if (missingFields.length > 0) {
        throw new Error(`Please fill in all required fields: ${missingFields.join(", ")}`);
      }
      
      const roomData = {
        roomNumber: data.roomNumber,
        category: data.category,
        price: parseFloat(data.price),
        status: data.status
      };
      
      if (data.description) {
        roomData.description = data.description;
      }
      
      const result = await addRoom(roomData);
      return [{ message: result.message || "Room added successfully!" }];
    }
  },
  {
    id: "updateRoom",
    label: "Update Room",
    icon: "✏️",
    color: "bg-orange-500 hover:bg-orange-600",
    description: "Update existing room details",
    hasInput: true,
    inputFields: [
      { name: "roomId", type: "text", placeholder: "Room ID (required)" },
      { name: "roomNumber", type: "text", placeholder: "Room Number" },
      { 
        name: "category", 
        type: "select", 
        placeholder: "Select Category",
        options: ["", "STANDARD", "DELUXE", "SUITE", "PREMIUM"]
      },
      { name: "price", type: "number", placeholder: "Price per night" },
      { 
        name: "status", 
        type: "select", 
        placeholder: "Room Status",
        options: ["", "AVAILABLE", "OCCUPIED", "MAINTENANCE"]
      },
      { name: "description", type: "textarea", placeholder: "Room description" }
    ],
    action: async (data) => {
      if (!data.roomId) {
        throw new Error("Room ID is required for updating");
      }
      
      // Remove empty fields
      const updateData = Object.fromEntries(
        Object.entries(data).filter(([key, value]) => key !== "roomId" && value && value.trim() !== "")
      );
      
      if (Object.keys(updateData).length === 0) {
        throw new Error("Please provide at least one field to update");
      }
      
      // Convert price to number if provided
      if (updateData.price) {
        updateData.price = parseFloat(updateData.price);
      }
      
      const result = await updateRoom(data.roomId, updateData);
      return [{ message: result.message || "Room updated successfully!" }];
    }
  },
  {
    id: "deleteRoom",
    label: "Delete Room",
    icon: "🗑️",
    color: "bg-red-500 hover:bg-red-600",
    description: "Remove a room from the system",
    hasInput: true,
    inputFields: [{ name: "roomId", type: "text", placeholder: "Enter Room ID to Delete" }],
    action: async (data) => {
      if (!data.roomId) {
        throw new Error("Please enter a Room ID to delete");
      }
      
      const result = await deleteRoom(data.roomId);
      return [{ message: result.message || "Room deleted successfully!" }];
    }
  },
  {
    id: "getAllReviews",
    label: "All Reviews",
    icon: "⭐",
    color: "bg-pink-500 hover:bg-pink-600",
    description: "View all customer reviews",
    action: async () => {
      const result = await getAllReviews();
      return Array.isArray(result) ? result : result.reviews || [];
    }
  },
  {
    id: "getReviewByUserId",
    label: "Reviews by User",
    icon: "👤",
    color: "bg-teal-500 hover:bg-teal-600",
    description: "View reviews by specific user",
    hasInput: true,
    inputFields: [{ name: "userId", type: "text", placeholder: "Enter User ID" }],
    action: async (data) => {
      if (!data.userId) {
        throw new Error("Please enter a User ID");
      }
      const result = await getReviewByUserId(data.userId);
      return Array.isArray(result) ? result : [result];
    }
  }
];

// Execute a manager action by ID
export const executeManagerAction = async (actionId, formData = {}) => {
  const action = managerActions.find(a => a.id === actionId);
  if (!action) {
    throw new Error(`Action ${actionId} not found`);
  }
  
  return await action.action(formData);
};
