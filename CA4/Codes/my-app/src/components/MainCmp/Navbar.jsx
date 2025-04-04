import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import SearchDropdown from "./SearchMenu";
import ToastNotification from "./ToastNotification";
import logoImage from "../../assets/logo.png";
import ProfileMenu from "./ProfileMenu";
import {
  faBook,
  faHistory,
  faShoppingCart,
  faSignOutAlt,
  faUser,
} from "@fortawesome/free-solid-svg-icons";

const Navbar = () => {
  const navigate = useNavigate();

  const [isProfileMenuVisible, setIsProfileMenuVisible] = useState(false);
  const [isSearchDropdownVisible, setIsSearchDropdownVisible] = useState(false);

  const toggleProfileMenu = () => {
    setIsProfileMenuVisible(!isProfileMenuVisible);
  };

  const toggleSearchDropdown = () => {
    setIsSearchDropdownVisible(!isSearchDropdownVisible);
  };

  const [user, setUser] = useState(null);

  useEffect(() => {
    try {
      const storedUser = JSON.parse(localStorage.getItem("user") || "{}");
      if (storedUser.username) {
        setUser(storedUser);
      }
    } catch (error) {
      console.error("Error parsing user from localStorage:", error);
    }
  }, []);

  const handleNavigation = (path) => {
    navigate(path);
    setIsProfileMenuVisible(false);
  };

  const handleLogout = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/logout", {
        method: "POST",
      });

      console.log("entered handle function");
      if (response.ok) {
        ToastNotification({ type: "success", message: "Logout successful!" });
        localStorage.removeItem("user");
        setUser(null);
        setTimeout(() => navigate("/SignIn"), 2000);
      } else {
        console.error("Logout failed");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <nav className="navbar bg-white border-bottom p-0">
      <div className="container-fluid d-flex justify-content-between align-items-center">
        <Link to="/" className="navbar-brand ps-4 m-0" href="#">
          <img src={logoImage} alt="MioBook Logo" width="79" height="29" />
        </Link>

        <div className="search-box d-none d-md-flex">
          <div className="dropdown">
            <button
              className="dropdown-toggle"
              type="button"
              id="searchDropdown"
              aria-expanded={isSearchDropdownVisible}
              onClick={toggleSearchDropdown}
            >
              Author
            </button>
            {isSearchDropdownVisible && (
              <ul
                className="dropdown-menu show"
                aria-labelledby="searchDropdown"
              >
                <SearchDropdown Name={"Name"} />
                <SearchDropdown Name={"Author"} />
                <SearchDropdown Name={"Genre"} />
              </ul>
            )}
          </div>

          <div className="divider"></div>

          <input type="text" placeholder="Search" />
        </div>

        <div className="profile-container d-flex align-items-center">
          {user ? (
            <>
              <button
                className="profile-btn"
                id="profileBtn"
                onClick={toggleProfileMenu}
              >
                {user.username.charAt(0).toUpperCase()}

              </button>
              <div
                className={`profile-menu ${isProfileMenuVisible ? "show" : ""}`}
                id="profileMenu"
              >
                <h6>{user.username}</h6>
                <hr />
                <ProfileMenu
                  Icon={faUser}
                  Title={"My Profile"}
                  onClick={() => handleNavigation("/user-profile")}
                />
                <ProfileMenu
                  Icon={faBook}
                  Title={"My Books"}
                  onClick={() => handleNavigation("/user-profile")}
                />
                <ProfileMenu
                  Icon={faShoppingCart}
                  Title={"Buy Cart"}
                  onClick={() => handleNavigation("/buycart")}
                />
                <ProfileMenu
                  Icon={faHistory}
                  Title={"Purchase History"}
                  onClick={() => handleNavigation("/purchase-history")}
                />
                <hr />
                <ProfileMenu
                  Icon={faSignOutAlt}
                  Title={"Logout"}
                  onClick={handleLogout}
                />
              </div>
            </>
          ) : (
            <Link to="/SignIn" className="btn btn-main m-0">
              Sign In
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
