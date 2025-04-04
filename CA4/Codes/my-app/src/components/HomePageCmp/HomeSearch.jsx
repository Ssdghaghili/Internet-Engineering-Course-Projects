import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const HomeSearch = () => {
  return (
    <div className="search-box d-flex d-md-none">
      <div className="dropdown">
        <button
          className="dropdown-toggle"
          type="button"
          id="searchDropdown"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          Author
        </button>
        <ul className="dropdown-menu" aria-labelledby="searchDropdown">
          <li>
            <a className="dropdown-item color-dark-gray" href="#">
              Name
            </a>
          </li>
          <li>
            <a className="dropdown-item color-dark-gray" href="#">
              Author
            </a>
          </li>
          <li>
            <a className="dropdown-item color-dark-gray" href="#">
              Genre
            </a>
          </li>
        </ul>
      </div>
      <div className="divider"></div>
      <input type="text" placeholder="Search" />
    </div>
  );
};

export default HomeSearch;
