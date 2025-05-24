import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const Footer = () => {
  return (
    <footer className="text-center bg-dark-green d-flex align-items-center justify-content-center py-1 mt-5">
      <p className="color-gray m-0" style={{ fontSize: "0.9rem" }}>
        Copyright © 2025 -
        <a href="#" className="text-decoration-none color-beige">
          MioBook
        </a>
      </p>
    </footer>
  );
};

Footer.propTypes = {};

export default Footer;
