import React, { useState, useEffect } from "react";
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

  useEffect(() => {}, [navigate]);

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

      localStorage.setItem("token", data.data.token);
      localStorage.setItem("role", data.data.role);

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

                <div className="col-10 col-sm-8 align-items-center mb-3 text-center">
                  <button
                    className="gsi-material-button rounded-3"
                    onClick={() => {
                      const clientId =
                        "406519361296-s9jjismdujr679fqoh9sadq9q1i1crd4.apps.googleusercontent.com";
                      const redirectUri = encodeURIComponent(
                        "http://localhost:3000/google/callback"
                      );
                      const scope = encodeURIComponent("openid email profile");
                      const authUrl =
                        `https://accounts.google.com/o/oauth2/v2/auth` +
                        `?response_type=code` +
                        `&client_id=${clientId}` +
                        `&redirect_uri=${redirectUri}` +
                        `&scope=${scope}` +
                        `&access_type=offline`;
                      window.location.href = authUrl;
                    }}
                  >
                    <div className="gsi-material-button-state"></div>
                    <div className="gsi-material-button-content-wrapper">
                      <div className="gsi-material-button-icon">
                        <svg
                          version="1.1"
                          xmlns="http://www.w3.org/2000/svg"
                          viewBox="0 0 48 48"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          style={{ display: "block" }}
                        >
                          <path
                            fill="#EA4335"
                            d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"
                          ></path>
                          <path
                            fill="#4285F4"
                            d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"
                          ></path>
                          <path
                            fill="#FBBC05"
                            d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"
                          ></path>
                          <path
                            fill="#34A853"
                            d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"
                          ></path>
                          <path fill="none" d="M0 0h48v48H0z"></path>
                        </svg>
                      </div>
                      <span className="gsi-material-button-contents">
                        Sign in with Google
                      </span>
                      <span style={{ display: "none" }}>
                        Sign in with Google
                      </span>
                    </div>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default SignIn;
