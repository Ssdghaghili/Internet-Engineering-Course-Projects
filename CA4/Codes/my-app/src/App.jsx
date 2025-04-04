import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import React from "react";
import HomePage from "./components/HomePage";
import SignUp from "./components/SignUp";
import SignIn from "./components/SignIn";
import UserProfile from "./components/UserProfile";
import BuyCart from "./components/BuyCart";
import PurchaseHistory from "./components/PurchaseHistory";
import { ToastContainer } from "react-toastify";


import "./App.css";

function App() {
  return (
    <Router>
      <ToastContainer />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/user-profile" element={<UserProfile />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/buycart" element={<BuyCart />} />
        <Route path="/purchase-history" element={<PurchaseHistory />} />
      </Routes>
    </Router>
  );
}

export default App;
