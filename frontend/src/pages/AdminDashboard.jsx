import { useAuth } from "../auth/AuthContext";

const AdminDashboard = () => {

    const { name } = useAuth();

    return <h1 className="text-2xl font-bold">Welcome, Admin : {name} !</h1>;
};

export default AdminDashboard;