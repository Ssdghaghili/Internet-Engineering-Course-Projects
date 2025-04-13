import React from "react";
import SearchDropdown from "../MainCmp/SearchMenu";
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
          Book
        </button>
        <ul className="dropdown-menu" aria-labelledby="searchDropdown">
          <SearchDropdown Name={"Book"} />
          <SearchDropdown Name={"Author"} />
        </ul>
      </div>
      <div className="divider"></div>
      <input type="text" placeholder="Search" />
    </div>
  );
};

export default HomeSearch;
