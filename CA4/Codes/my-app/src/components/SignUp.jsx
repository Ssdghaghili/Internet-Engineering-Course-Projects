import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Footer from "./MainCmp/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import ToastNotification from "./MainCmp/ToastNotification";
import {
  faCircleUser,
  faBriefcase,
  faEye,
  faEyeSlash,
} from "@fortawesome/free-solid-svg-icons";

const SignUp = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [country, setCountry] = useState("");
  const [city, setCity] = useState("");
  const [userType, setUserType] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => {
    setShowPassword((prevState) => !prevState);
  };

    const [errors, setErrors] = useState({});

  const isFormValid =
    userType !== "" &&
    username.trim() !== "" &&
    password.trim() !== "" &&
    email.trim() !== "" &&
    country.trim() !== "" &&
    city.trim() !== "";

  const selectButton = (e, type) => {
    if (userType === type) {
      e.currentTarget.classList.remove("active");
      setUserType("");
    } else {
      document.querySelectorAll(".btn-iam").forEach((button) => {
        button.classList.remove("active");
      });

      e.currentTarget.classList.add("active");
      setUserType(type);
    }
  };

  const handleSignUp = async (e) => {
    e.preventDefault();

    const signUpData = {
      username,
      password,
      email,
      role: userType,
      address: {
        country,
        city,
      },
    };

    console.log(signUpData);

    try {
      const response = await fetch("http://localhost:8080/api/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(signUpData),
      });

      const data = await response.json();
    //   console.log(data);

      if (!data.success) {
        if (data.message.toLowerCase().includes("username")) {
          setErrors({ username: data.message });
        } else if (data.message.toLowerCase().includes("email")) {
          setErrors({ email: data.message });
        } else if (data.message.toLowerCase().includes("password")) {
          setErrors({ password: data.message });
        }
        // console.log(data.message);
        return;
      }

      ToastNotification({ type: "success", message: "SignUp was succesful" });

      setErrors({});
      setTimeout(() => navigate("/signin"), 2000);
    } catch (error) {
      console.error("Error:", error.message);
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
                  <h4>Sign Up</h4>
                </div>
                <div className="col-12 text-center">
                  <p className="font-small color-dark-gray">MioBook</p>
                </div>

                <div className="col-sm-8 align-items-center">
                  <div className="form-floating mb-3">
                    <input
                      type="Username"
                      className={`form-control ${
                        errors.username ? "is-invalid" : ""
                      }`}
                      id="floatingInput"
                      placeholder=""
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                    />
                    <label htmlFor="floatingInput">Username</label>
                    {errors.username && (
                      <div className="invalid-feedback">{errors.username}</div>
                    )}
                  </div>
                </div>

                <div className="col-sm-8 align-items-center">
                  <div className="form-floating mb-3">
                    <input
                      type={showPassword ? "text" : "password"}
                      className={`form-control ${
                        errors.password ? "is-invalid" : ""
                      }`}
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
                    {errors.password && (
                      <div className="invalid-feedback">{errors.password}</div>
                    )}
                  </div>
                </div>

                <div className="col-sm-8 align-items-center">
                  <div className="form-floating mb-3">
                    <input
                      type="Email"
                      className={`form-control ${
                        errors.email ? "is-invalid" : ""
                      }`}
                      id="floatingEmail"
                      placeholder="name@example.com"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                    />
                    <label htmlFor="floatingInput">Email</label>
                    {errors.email && (
                      <div className="invalid-feedback">{errors.email}</div>
                    )}
                  </div>
                </div>

                <div className="row d-flex justify-content-center p-0 m-0">
                  <div className="col-sm-8 col-md-4 align-items-center me-1">
                    <div className="form-floating mb-3">
                      <input
                        type="Country"
                        className="form-control"
                        id="floatingCountry"
                        placeholder=""
                        value={country}
                        onChange={(e) => setCountry(e.target.value)}
                      />
                      <label htmlFor="floatingInput">Country</label>
                    </div>
                  </div>

                  <div className="col-sm-8 col-md-4 align-items-center">
                    <div className="form-floating mb-3">
                      <input
                        type="City"
                        className="form-control"
                        id="floatingCity"
                        placeholder=""
                        value={city}
                        onChange={(e) => setCity(e.target.value)}
                      />
                      <label htmlFor="floatingInput">City</label>
                    </div>
                  </div>
                </div>

                <div className="col-sm-8 text-center text-sm-start">
                  <p className="ms-1">I am</p>
                </div>

                <div className="row justify-content-center p-0 m-0">
                  <div className="col-5 col-sm-4 align-items-center me-1">
                    <button
                      //   className="btn btn-iam d-flex align-items-center justify-content-center p-2 gap-2"
                      className={`btn btn-iam d-flex align-items-center justify-content-center p-2 gap-2 ${
                        userType === "customer" ? "active" : ""
                      }`}
                      type="button"
                      onClick={(e) => selectButton(e, "customer")}
                    >
                      <FontAwesomeIcon icon={faCircleUser} /> Customer
                    </button>
                  </div>

                  <div className="col-5 col-sm-4 align-items-center me-1">
                    <button
                      //   className="btn btn-iam d-flex align-items-center justify-content-center p-2 gap-2"
                      className={`btn btn-iam d-flex align-items-center justify-content-center p-2 gap-2 ${
                        userType === "admin" ? "active" : ""
                      }`}
                      type="button"
                      onClick={(e) => selectButton(e, "admin")}
                    >
                      <FontAwesomeIcon icon={faBriefcase} /> Manager
                    </button>
                  </div>

                  <div className="col-10 col-sm-8 align-items-center mb-1 px-2">
                    <div className="d-grid gap-2 col-12 mx-auto">
                      <button
                        className="btn btn-sign"
                        type="button"
                        disabled={!isFormValid}
                        onClick={handleSignUp}
                      >
                        Sign up
                      </button>
                    </div>
                  </div>

                  <div className="col-sm-6 justify-content-center mb-1 d-flex font-small">
                    <p className="me-1 color-dark-gray">
                      Already have an accunt?
                    </p>
                    <Link
                      to="/signin"
                      className="text-decoration-none font-size9 fw-bold color-green"
                    >
                      Sign in
                    </Link>
                  </div>
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

export default SignUp;
