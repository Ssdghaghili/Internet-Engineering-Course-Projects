import React from "react";
import { Navigate } from "react-router-dom";

function ProtectedRoute({ children, allowedRoles }) {
  const storedUser = JSON.parse(localStorage.getItem("user") || "{}");

  if (!storedUser.username) {
    return <Navigate to="/signin" />;
  }

  if (allowedRoles && !allowedRoles.includes(storedUser.role)) {
    return <Navigate to="/error" />;
  }

  return children;
}

export default ProtectedRoute;
