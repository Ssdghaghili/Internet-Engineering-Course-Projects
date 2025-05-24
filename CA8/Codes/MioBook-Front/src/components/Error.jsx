import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import ErrorPic from "../assets/ERROR Background Removed.png";
import { Link, useLocation } from "react-router-dom";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";

const Error = (props) => {
  const location = useLocation();

  const isNotFound = location.pathname !== "/error";

  const title = props.ErrorType || (isNotFound ? "404" : "Error");
  const message =
    props.ErrorMessage ||
    (isNotFound
      ? "Oops! The page you are looking for does not exist."
      : "An unexpected error occurred. Please try again later.");

  return (
    <>
      <Navbar />
      <div
        className="container d-flex flex-column align-items-center justify-content-center min-vh-100"
        style={{ height: "80vh" }}
      >
        <div className="row">
          <div className="col-12 col-md-6 d-flex flex-column align-items-center justify-content-center ">
            <h1 id="error-title">{title}</h1>
            <p id="error-message" className="text-center">{message}</p>
            <Link to="/" className="btn btn-add-to-cart">
              Go back to Home
            </Link>
          </div>
          <div className="d-none d-md-block col-md-6">
            <img src={ErrorPic} alt="Error Illustration" height="100%" width="100%" />
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Error;