import React, { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import ToastNotification from "../components/MainCmp/ToastNotification";

const GoogleCallback = () => {
  const navigate = useNavigate();
  const didCallRef = useRef(false);

  useEffect(() => {
    if (didCallRef.current) return;
    didCallRef.current = true;

    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");

    if (!code) {
      ToastNotification({ type: "error", message: "Code not found in URL!" });
      navigate("/signin");
      return;
    }

    const exchangeCodeForToken = async () => {
      try {
        const response = await fetch("/api/google/callback", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ code }),
        });

        if (!response.ok) {
          const errorData = await response.json();
          ToastNotification({ type: "error", message: errorData.message });
          navigate("/signin");
          return;
        }

        const data = await response.json();

        if (!data.success) {
          ToastNotification({ type: "error", message: data.message });
          navigate("/signin");
          return;
        }

        localStorage.setItem("token", data.data.token);
        localStorage.setItem("role", data.data.role);

        ToastNotification({ type: "success", message: "Welcome back! 🎉" });
        setTimeout(() => navigate("/"), 100);
      } catch (error) {
        console.error("Error during Google OAuth callback:", error);
        ToastNotification({ type: "error", message: "Something went wrong!" });
        navigate("/signin");
      }
    };

    exchangeCodeForToken();
  }, [navigate]);

  return (
    <div className="d-flex vh-100 justify-content-center align-items-center">
      <h4>Redirecting...</h4>
    </div>
  );
};

export default GoogleCallback;
