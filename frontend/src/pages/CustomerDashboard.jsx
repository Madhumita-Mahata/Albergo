import { useAuth } from "../../context/AuthContext";

const CustomerDashboard = () => {

    const { name } = useAuth();

    return <h1 className="text-2xl font-bold">Welcome, User : {name} !</h1>;
};

export default CustomerDashboard;