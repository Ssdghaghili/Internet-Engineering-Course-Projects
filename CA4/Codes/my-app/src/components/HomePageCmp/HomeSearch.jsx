import { useState } from "react";
import { useNavigate } from "react-router-dom";
import React from "react";
import SearchDropdown from "../MainCmp/SearchMenu";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const HomeSearch = () => {
  const navigate = useNavigate();
  const [isSearchDropdownVisible, setIsSearchDropdownVisible] = useState(false);
  const [searchKey, setSearchKey] =  useState("Book");

  const toggleSearchDropdown = () => {
    setIsSearchDropdownVisible(!isSearchDropdownVisible);
  };

  return (
      <div className="search-box d-flex d-md-none">
        <div className="dropdown">
          <button className="dropdown-toggle" type="button" id="searchDropdown" aria-expanded={isSearchDropdownVisible} onClick={toggleSearchDropdown}> {searchKey} </button> {isSearchDropdownVisible && ( <ul className="dropdown-menu show" aria-labelledby="searchDropdown">
            <li>
              <a className="dropdown-item color-dark-gray" href="#" onClick={()=> { setSearchKey("Book"); toggleSearchDropdown() }} >Book</a>
            </li>
            <li>
              <a className="dropdown-item color-dark-gray" href="#" onClick={()=> { setSearchKey("Author"); toggleSearchDropdown() }} >Author</a>
            </li>
          </ul> )}
        </div>
        <div className="divider"></div>
        <input type="text" placeholder="Search" onKeyDown={(e)=> { if (e.key === "Enter") { if (searchKey === "Book") { navigate(`/books/search?title=${e.target.value}`); } else { navigate(`/Author/${e.target.value}`); } } }} />
      </div>
  );
};

export default HomeSearch;
