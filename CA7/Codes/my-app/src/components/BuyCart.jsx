import { useEffect, useState } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";
import CartItem from "./MainCmp/CartItem";
import BookImage from "../assets/book-image.jpeg";
import NoCart from "../assets/no-cart.png";
import ToastNotification from "./MainCmp/ToastNotification";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCartShopping } from "@fortawesome/free-solid-svg-icons";

const BuyCart = () => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [cartItems, setCartItems] = useState([]);

  const fetchCart = async () => {
    fetch(`/api/user/cart`, {
      method: "GET",
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    })
    .then((res) => {
      if (!res.ok) {
        throw new Error("Network response was not ok");
      }
      return res.json();
    })
    .then((data) => {
      setCartItems(data.data);
      setLoading(false);
    })
    .catch(() => {
     setError("Failed to load book details.");
     setLoading(false);
    });
  }

  useEffect(() => {
    fetchCart();
  }, []);

  if (loading) {
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

  const handlePurchase = () => {
    fetch(`/api/user/purchase`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': localStorage.getItem("token")
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          ToastNotification({
            type: "success",
            message: `Purchase successful!`,
          });
          setCartItems(null);
        } else {
          ToastNotification({
            type: "error",
            message: data.message || "Failed to purchase books.",
          });
        }
      })
      .catch((error) => {
        console.error("Error purchasing books:", error);
        ToastNotification({
          type: "error",
          message: "An unexpected error occurred. Please try again later.",
        });
      });
   window.dispatchEvent(new Event("purchaseSuccess"));
  };

  return (
    <>
      <Navbar />
      <div className="min-vh-100">
        <div className="row mt-5 justify-content-center bg">
          <div className="col-10 show_area p-0">
            <div className="row d-flex justify-content-start">
              <div className="col-12 ms-4 ps-3">
                <h3 className="ms-4 mt-3 mb-4 text-start fw-semibold">
                  <FontAwesomeIcon className="pe-3" icon={faCartShopping} />
                  Cart
                </h3>
              </div>
            </div>

            {cartItems === null || cartItems.items.length === 0 ? (
              <div className="row d-flex justify-content-center">
                <img
                  src={NoCart}
                  alt="No Cart"
                  style={{ width: "300px", height: "300px" }}
                  className="mt-2 "
                />
              </div>
            ) : (
              <div className="row d-flex justify-content-center mt-3 ">
                <div className="col-11 bg-gray ps-4 pe-4 rounded-top-2">
                  <div className="row color-light-black fw-light font-small pt-2 pb-2">
                    <div className="col-1">Image</div>
                    <div className="col-3">Name</div>
                    <div className="col-2 ps-0">Author</div>
                    <div className="col-2 ps-0">Price</div>
                    <div className="col-2 ps-0">Borrow Days</div>
                    <div className="col-3"></div>
                  </div>
                </div>

                <div className="col-11 ps-4 pe-4  mb-1">
                  <div className="row fw-light font-small pt-2 pb-2">
                    {cartItems.items.map((item, index) => (
                      <CartItem key={index} Item={item} Image={BookImage} onRemove={fetchCart} />
                    ))}
                  </div>
                  <div className="row mb-2 d-flex justify-content-center">
                    <div className="col-12 d-flex justify-content-end rounded-bottom-2">
                      <p className="mt-1 me-4 font-semi-samll olor-light-black fw-light p-0">
                        Total Cart Price: $
                        {(cartItems.totalCost / 100).toFixed(2)}
                      </p>
                    </div>
                  </div>
                  <div className="row d-flex justify-content-center mt-2">
                    <button
                      className="btn btn-main w-25"
                      type="button"
                      disabled={cartItems.items.length === 0}
                      onClick={handlePurchase}
                    >
                      Purchase
                    </button>
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

export default BuyCart;
