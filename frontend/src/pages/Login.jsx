import { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await axios.post("http://localhost:8080/auth/login", {
        email,
        password,
      });

      const { token, role, name, id } = res.data;

      login({ token, role, name, id, email });

      // Save user info to localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("id", id);
      localStorage.setItem("userId", id); // Important for review submission
      localStorage.setItem("name", name);
      localStorage.setItem("email", email);

      // Check if user was redirected from a protected route
      const redirectPath = sessionStorage.getItem("redirectAfterLogin");

      if (redirectPath) {
        sessionStorage.removeItem("redirectAfterLogin");
        navigate(redirectPath);
      } else {
        // Navigate based on role
        if (role === "USER") navigate("/user/dashboard");
        else if (role === "OWNER") navigate("/owner/dashboard");
        else if (role === "ADMIN") navigate("/admin/dashboard");
        else navigate("/"); // fallback
      }
    } catch (err) {
      alert("Login failed");
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="p-4 max-w-md mx-auto mt-10 space-y-4"
    >
      <input
        type="email"
        placeholder="Email"
        className="w-full p-2 border rounded"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        className="w-full p-2 border rounded"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <button
        type="submit"
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Login
      </button>
    </form>
  );
};

export default Login;