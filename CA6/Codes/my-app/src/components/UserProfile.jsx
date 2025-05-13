import { useEffect, useState } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";
import { useNavigate } from "react-router-dom";
import ToastNotification from "./MainCmp/ToastNotification";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import BookImage from "../assets/book-image.jpeg";
import NoBookImage from "../assets/no-history.png";
import { faBookOpen } from "@fortawesome/free-solid-svg-icons";
import {
  faEnvelope as faEnvelopeEmpty,
  faUser as faUserEmpty,
} from "@fortawesome/free-regular-svg-icons";
import UserBookItem from "./MainCmp/UserBookItem";

const UserProfile = () => {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [userLoading, setUserLoading] = useState(true);
  const [cartLoading, setCartLoading] = useState(true);
  const [error, setError] = useState(null);
  const [book, setBook] = useState(null);
  const [amount, setAmount] = useState("");

  const canAddAmount = amount.trim().length > 0;

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetch(`/api/user`);
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setUser(data.data);
      } catch (err) {
        console.error("Failed to load user:", err);
        setError("Failed to load user.");
      } finally {
        setUserLoading(false);
      }
    };

    fetchUser();
  }, []);

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const res = await fetch(
          `/api/user/purchased-books`
        );
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setBook(data.data);
      } catch (err) {
        console.error("Failed to load cart:", err);
        setError("Failed to load cart.");
      } finally {
        setCartLoading(false);
      }
    };
    fetchCart();
  }, [user]);

  if (userLoading || cartLoading) {
    return (
      <div className="container mt-5 text-center">
        <Navbar />
        <h3>Loading book details...</h3>
      </div>
    );
  }
  if (error) {
    return (
      <>
        <Navbar />
        <div className="container mt-5 text-center">
          <h3>{error}</h3>
        </div>
      </>
    );
  }

  const handleLogout = async () => {
    try {
      const response = await fetch("/api/logout", {
        method: "POST",
      });
      if (response.ok) {
        ToastNotification({
          type: "success",
          message: "Logout successful!",
        });

        localStorage.removeItem("user");
        // setUser(null);
        navigate("/signin");
      } else {
        console.error("Logout failed");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const handleAddAmount = async () => {
    try {
      const response = await fetch(
        `/api/user/addCredit?amount=${amount * 100}`,
        {
          method: "POST",
        }
      );

      const data = await response.json();

      if (response.ok) {
        if (!data.success) {
          ToastNotification({ type: "error", message: data.message });
        } else {
          ToastNotification({
            type: "success",
            message: "Credit added successfully",
          });
          setUser(data.data);
        }
      } else {
        console.error("Failed to add credit");
        const data = await response.json();
        ToastNotification({
          type: "error",
          message: data.message || "Failed to add credit.",
        });
      }
      setAmount("");
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <>
      <Navbar />

      <div className="min-vh-100 pt-5">
        <div className="row justify-content-center">
          <div className="col-md-10">
            <div className="row mt-md-0 mt-3 ms-md-0 ms-5 me-md-0 me-5">
              <div className="col-12 col-md-7 bg-white p-4 rounded-3">
                <div className="mb-3">
                  <h3 className="fw-bold">
                    ${(user.balance / 100).toFixed(2)}
                  </h3>
                </div>
                <div className="input-group">
                  <input
                    type="number"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    className="form-control border rounded-3 ps-4 p-2 form-amount mt-3 color-dark-gray text-black-50"
                    placeholder="$Amount"
                  />
                  <button
                    type="button"
                    className="btn-add-amount rounded-3 ms-2 p-2 px-3 mt-3"
                    onClick={handleAddAmount}
                    disabled={!canAddAmount}
                  >
                    Add more credit
                  </button>
                </div>
              </div>

              <div className="col-1 d-md-block d-none"></div>

              <div
                className="col-12 col-md-4 bg-white p-4 rounded-3 mb-3 mb-md-0 mt-md-0 mt-5"
                style={{ minWidth: "280px" }}
              >
                <div className="mb-3 d-flex align-items-center">
                  <FontAwesomeIcon className="pe-3" icon={faUserEmpty} />
                  <span>{user.username}</span>
                </div>
                <div className="mb-3 d-flex align-items-center">
                  <FontAwesomeIcon className="pe-3" icon={faEnvelopeEmpty} />
                  <span>{user.email}</span>
                </div>
                <button
                  className="btn-gray p-2 fw-medium px-4"
                  type="button"
                  onClick={handleLogout}
                >
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="row mt-5 justify-content-center bg">
          <div className="col-10 show_area p-0">
            <div className="row d-flex justify-content-start">
              <div className="col-12 ms-4 ps-3">
                <h3 className="ms-4 mt-3 mb-4 text-start fw-semibold">
                  <FontAwesomeIcon className="pe-3" icon={faBookOpen} />
                  My Books
                </h3>
              </div>
            </div>
            {book.length === 0 || !book ? (
              <div className="row d-flex justify-content-center">
                <img
                  src={NoBookImage}
                  alt="No book"
                  style={{ width: "350px", height: "350px" }}
                  className="mt-2 "
                />
              </div>
            ) : (
              <div className="row d-flex justify-content-center mt-3 ">
                <div className="col-11 bg-gray ps-4 pe-4 rounded-top-2">
                  <div className="row color-light-black fw-light font-small pt-2 pb-2">
                    <div className="col-1 ps-0">Image</div>
                    <div className="col-2 ps-0">Name</div>
                    <div className="col-1 ps-0">Author</div>
                    <div className="col-2 ps-0">Genre</div>
                    <div className="col-2 ps-0 m-0 pe-0">Publisher</div>
                    <div className="col-1 ps-0 pe-0 text-start">
                      Published Year
                    </div>
                    <div className="col-2 pe-5 text-center">Status</div>
                    <div className="col-1"></div>
                  </div>
                </div>
                {/* {console.log(book)} */}

                <div className="col-11 ps-4 pe-4  mb-4">
                  <div className="row fw-light font-small pt-2 pb-2">
                    {book.map((bookItem, index) => (
                      <UserBookItem
                        key={index}
                        Item={bookItem}
                        Image={BookImage}
                      />
                    ))}
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default UserProfile;
