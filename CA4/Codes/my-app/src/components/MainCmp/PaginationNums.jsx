import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const PaginationNums = ({ page, selectedPage }) => {
    return (
      <li className={`page-item ${page === selectedPage ? "active" : ""}`}>
        <a className="page-link pagination-link" href="#">
          {page}
        </a>
      </li>
    );
  };

export default PaginationNums;
