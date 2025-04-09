import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Footer from "./MainCmp/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import ToastNotification from "./MainCmp/ToastNotification";

const SignIn = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [errors, setErrors] = useState({});

  const togglePasswordVisibility = () => {
    setShowPassword((prevState) => !prevState);
  };

  const isFormValid = username.trim() !== "" && password.trim() !== "";

  const handleSignIn = async (e) => {
    e.preventDefault();

    const signInData = { username, password };

    try {
      const response = await fetch("api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(signInData),
      });

      const data = await response.json();

      if (!data.success) {
        setErrors({ username: data.message });
        return;
      }

      // console.log("Login successful:", data);

      localStorage.setItem("user", JSON.stringify(data.data.data));
      
      // console.log("User data:", data.data.data);

      ToastNotification({ type: "success", message: "Welcome back! 🎉" });

      setErrors({});
      setTimeout(() => navigate("/"), 2000);

      
    } catch (error) {
      console.error("Error:", error.message);
      setErrors({
        username: "An error occurred while connecting to the server.",
      });
    }
  };

  return (
    <>
      <div className="bg-light">
        <div className="container-fluid">
          <div className="row d-flex vh-100 align-items-center justify-content-center">
            <div className="col-sm-12 col-md-10 col-lg-8 col-xl-6 p-4 d-flex justify-content-center rounded-4 bg-white">
              <div className="row justify-content-center">
                <div className="col-12 text-center">
                  <h4>Sign in</h4>
                </div>
                <div className="col-12 text-center">
                  <p className="font-small color-dark-gray">MioBook</p>
                </div>

                <div className="col-sm-8 align-items-center">
                  <div className="form-floating mb-3">
                    <input
                      type="text"
                      className="form-control"
                      id="floatingInput"
                      placeholder="name@example.com"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                    />
                    <label htmlFor="floatingInput">Username</label>
                  </div>
                </div>

                <div className="col-sm-8 align-items-center">
                  <div className="form-floating mb-3">
                    <input
                      type={showPassword ? "text" : "password"}
                      className="form-control"
                      id="floatingPassword"
                      placeholder="Password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    />
                    <label htmlFor="floatingInput">Password</label>
                    <span
                      className="position-absolute top-50 end-0 translate-middle-y me-3"
                      style={{ cursor: "pointer" }}
                      onClick={togglePasswordVisibility}
                    >
                      <FontAwesomeIcon
                        icon={showPassword ? faEye : faEyeSlash}
                      />
                    </span>
                  </div>
                </div>

                {/* نمایش ارور بالای دکمه Sign in */}
                {errors.username && (
                  <div className="col-10 col-sm-8 align-items-center mb-1 mt-2">
                    <div className="text-danger text-center" role="alert">
                      {errors.username}
                    </div>
                  </div>
                )}

                <div className="col-10 col-sm-8 align-items-center mb-1">
                  <div className="d-grid gap-2 col-12 mx-auto">
                    <button
                      className="btn btn-sign"
                      type="button"
                      disabled={!isFormValid}
                      onClick={handleSignIn}
                    >
                      Sign in
                    </button>
                  </div>
                </div>

                <div className="col-sm-6 justify-content-center mb-1 d-flex font-small">
                  <p className="me-1 color-dark-gray">Not a member yet?</p>
                  <Link
                    to="/signup"
                    className="text-decoration-none font-size9 fw-bold color-green"
                  >
                    Sign up
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Footer />
      </div>
    </>
  );
};

export default SignIn;
