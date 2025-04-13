import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import React from "react";
import HomePage from "./components/HomePage";
import SignUp from "./components/SignUp";
import SignIn from "./components/SignIn";
import UserProfile from "./components/UserProfile";
import BuyCart from "./components/BuyCart";
import PurchaseHistory from "./components/PurchaseHistory";
import BookPage from "./components/BookPage";
import SearchResult from "./components/SearchResult";
import Error from "./components/Error";
import ProtectedRoute from "./components/MainCmp/ProtectedRoute";
import BookContent from "./components/BookContent";
import AuthorPage from "./components/AuthorPage";
import { ToastContainer } from "react-toastify";

import "./App.css";

function App() {
  return (
    <Router>
      <ToastContainer />
      <Routes>
        {/* Public Routes */}
        <Route path="/signup" element={<SignUp />} />
        <Route path="/signin" element={<SignIn />} />

        {/* Protected Routes for Both Admin & Customer */}
        <Route
          path="/"
          element={
            <ProtectedRoute allowedRoles={["admin", "customer"]}>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/Book/:bookSlug"
          element={
            <ProtectedRoute allowedRoles={["admin", "customer"]}>
              <BookPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/books/search"
          element={
            <ProtectedRoute allowedRoles={["admin", "customer"]}>
              <SearchResult />
            </ProtectedRoute>
          }
        />
        <Route
          path="/Author/:authorSlug"
          element={
            <ProtectedRoute allowedRoles={["admin", "customer"]}>
              <AuthorPage />
            </ProtectedRoute>
          }
        />

        {/* Protected Routes for Customer only */}
        <Route
          path="/user-profile"
          element={
            <ProtectedRoute allowedRoles={["customer"]}>
              <UserProfile />
            </ProtectedRoute>
          }
        />
        <Route
          path="/buycart"
          element={
            <ProtectedRoute allowedRoles={["customer"]}>
              <BuyCart />
            </ProtectedRoute>
          }
        />
        <Route
          path="/purchase-history"
          element={
            <ProtectedRoute allowedRoles={["customer"]}>
              <PurchaseHistory />
            </ProtectedRoute>
          }
        />
        <Route
          path="/Book/:bookSlug/content"
          element={
            <ProtectedRoute allowedRoles={["customer"]}>
              <BookContent />
            </ProtectedRoute>
          }
        />

        {/* Protected Routes for Admin only */}
        {/* <Route
          path="/admin-dashboard"
          element={
            <ProtectedRoute allowedRoles={["admin"]}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        /> */}

        {/* Fallback */}
        <Route path="/error" element={<Error />} />
        <Route path="*" element={<Error />} />
      </Routes>
    </Router>
  );
}

export default App;
