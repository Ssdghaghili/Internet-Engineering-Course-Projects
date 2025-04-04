import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";

const UserProfile = () => {
    return (<>
        <Navbar />
        <h1> User profile page</h1>;
        <Footer />
    </>)
};

export default UserProfile;
