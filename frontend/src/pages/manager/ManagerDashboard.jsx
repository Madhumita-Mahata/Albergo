import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import RoleLayout from "../../components/RoleLayout";
import { managerActions, executeManagerAction } from "../../components/ManagerActions";

export default function ManagerDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  
  // State management for manager operations
  const [loading, setLoading] = useState(false);
  const [selectedAction, setSelectedAction] = useState(null);
  const [formData, setFormData] = useState({});
  const [results, setResults] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user || user.role !== "MANAGER") {
      navigate("/login");
      return;
    }
  }, [user, navigate]);

  // Handle action button clicks
  const handleActionClick = (action) => {
    setSelectedAction(action);
    setFormData({});
    setError("");
    setResults(null);
    
    // Execute immediately if no input is required
    if (!action.hasInput) {
      executeAction(action.id, {});
    }
  };

  // Handle form input changes
  const handleInputChange = (name, value) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    if (selectedAction) {
      executeAction(selectedAction.id, formData);
    }
  };

  // Handle cancel action
  const handleCancel = () => {
    setSelectedAction(null);
    setFormData({});
    setError("");
    setResults(null);
  };

  // Execute manager actions with error handling and loading states
  const executeAction = async (actionId, data) => {
    setLoading(true);
    setError("");
    
    try {
      const result = await executeManagerAction(actionId, data);
      setResults(result);
      
      // Clear form data for successful operations that modify data
      if (['addRoom', 'updateRoom', 'deleteRoom'].includes(actionId)) {
        setFormData({});
        setSelectedAction(null);
      }
      
    } catch (err) {
      setError(err.message || "An error occurred while processing your request");
      setResults(null);
    } finally {
      setLoading(false);
    }
  };

  // Custom results renderer for manager-specific data
  const renderResults = (results) => {
    if (results[0]?.message) {
      return (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-lg">
          {results[0].message}
        </div>
      );
    }

    // Handle rooms display
    if (results[0]?.roomId || results[0]?.roomNumber) {
      return (
        <div className="overflow-x-auto">
          <table className="min-w-full border-collapse border border-gray-300">
            <thead className="bg-gray-50">
              <tr>
                <th className="border border-gray-300 px-4 py-2 text-left">Room ID</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Room Number</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Category</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Price</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Status</th>
              </tr>
            </thead>
            <tbody>
              {results.map((room, index) => (
                <tr key={room.roomId || index} className="hover:bg-gray-50">
                  <td className="border border-gray-300 px-4 py-2">{room.roomId}</td>
                  <td className="border border-gray-300 px-4 py-2">{room.roomNumber}</td>
                  <td className="border border-gray-300 px-4 py-2">
                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${
                      room.category === 'SUITE' ? 'bg-purple-100 text-purple-800' :
                      room.category === 'DELUXE' ? 'bg-blue-100 text-blue-800' :
                      room.category === 'PREMIUM' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {room.category}
                    </span>
                  </td>
                  <td className="border border-gray-300 px-4 py-2">${room.price}</td>
                  <td className="border border-gray-300 px-4 py-2">
                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${
                      room.status === 'AVAILABLE' ? 'bg-green-100 text-green-800' :
                      room.status === 'OCCUPIED' ? 'bg-red-100 text-red-800' :
                      'bg-yellow-100 text-yellow-800'
                    }`}>
                      {room.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      );
    }

    // Handle reviews display
    if (results[0]?.reviewId || results[0]?.rating) {
      return (
        <div className="overflow-x-auto">
          <table className="min-w-full border-collapse border border-gray-300">
            <thead className="bg-gray-50">
              <tr>
                <th className="border border-gray-300 px-4 py-2 text-left">Review ID</th>
                <th className="border border-gray-300 px-4 py-2 text-left">User</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Rating</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Comment</th>
                <th className="border border-gray-300 px-4 py-2 text-left">Date</th>
              </tr>
            </thead>
            <tbody>
              {results.map((review, index) => (
                <tr key={review.reviewId || index} className="hover:bg-gray-50">
                  <td className="border border-gray-300 px-4 py-2">{review.reviewId}</td>
                  <td className="border border-gray-300 px-4 py-2">
                    {review.user?.firstName} {review.user?.lastName}
                  </td>
                  <td className="border border-gray-300 px-4 py-2">
                    <div className="flex items-center">
                      {[...Array(5)].map((_, i) => (
                        <span key={i} className={`text-lg ${
                          i < review.rating ? 'text-yellow-400' : 'text-gray-300'
                        }`}>
                          ⭐
                        </span>
                      ))}
                      <span className="ml-2 text-sm text-gray-600">({review.rating}/5)</span>
                    </div>
                  </td>
                  <td className="border border-gray-300 px-4 py-2">{review.comment}</td>
                  <td className="border border-gray-300 px-4 py-2">{review.date}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      );
    }

    // Default table rendering for other data types
    return null;
  };

  return (
    <RoleLayout 
      roleName="Manager"
      actions={managerActions}
      selectedAction={selectedAction}
      formData={formData}
      results={results}
      error={error}
      loading={loading}
      onActionClick={handleActionClick}
      onInputChange={handleInputChange}
      onSubmit={handleSubmit}
      onCancel={handleCancel}
      renderResults={renderResults}
    />
  );
}

