import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const PaginationNums = ({ page, selectedPage, changePage }) => {
    return (
      <li className="page-item">
        <a className={`d-inline-flex justify-content-center align-items-center page-link pagination-link ${page === selectedPage ? "active" : ""}`} href="#" onClick={changePage} >
          {page}
        </a>
      </li>
    );
  };

export default PaginationNums;
