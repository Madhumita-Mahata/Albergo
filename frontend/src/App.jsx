import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
// import Register from "./pages/Register";

import UserLayout from "./layouts/CustomerLayout";
import ManagerLayout from "./layouts/ManagerLayout";
import AdminLayout from "./layouts/AdminLayout";

//import UserDashboard from "./pages/user/UserDashboard";
import ManagerDashboard from "./pages/ManagerDashboard";
import AdminDashboard from "./pages/AdminDashboard";

import NotFound from "./pages/NotFound";
import PrivateRoute from "./routes/PrivateRoute";
import CustomerDashboard from "./pages/customer/CustomerDashboard";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<Login />} />
        {/* <Route path="/register" element={<Register />} /> */}

        {/* User Routes */}
        <Route
          path="/user/*"
          element={
            <PrivateRoute allowedRole="CUSTOMER">
              <UserLayout />
            </PrivateRoute>
          }
        >
          <Route path="dashboard" element={<CustomerDashboard />} />
        </Route>

        {/* Manager Routes */}
        <Route
          path="/manager/*"
          element={
            <PrivateRoute allowedRole="MANAGER">
              <ManagerLayout />
            </PrivateRoute>
          }
        >
          <Route path="dashboard" element={<ManagerDashboard />} />
        </Route>

        {/* Admin Routes */}
        <Route
          path="/admin/*"
          element={
            <PrivateRoute allowedRole="ADMIN">
              <AdminLayout />
            </PrivateRoute>
          }
        >
          <Route path="dashboard" element={<AdminDashboard />} />
        </Route>

        {/* Fallback Route */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;