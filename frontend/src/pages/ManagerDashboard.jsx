import { useAuth } from "../auth/AuthContext";

const ManagerDashboard = () => {

    const { name } = useAuth();

    return <h1 className="text-2xl font-bold">Welcome, Manager : {name} !</h1>;
};

export default ManagerDashboard;