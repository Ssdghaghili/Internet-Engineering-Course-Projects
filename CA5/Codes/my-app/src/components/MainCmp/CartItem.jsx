import React from "react";
import ToastNotification from "./ToastNotification";

const CartItem = ({ Item, Image, onRemove }) => {
  const handelRemoveButtonClick = () => {
    fetch(`/api/user/removeCart?title=${Item.title}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          onRemove();
          ToastNotification({
            type: "success",
            message: `${Item.title} removed successfully!`,
          });
        } else {
          ToastNotification({
            type: "error",
            message: data.message || "Failed to remove book to cart.",
          });
          console.error("Backend message:", data.message);
        }
      })
      .catch((error) => {
        console.error("Error removing book to cart:", error);
        ToastNotification({
          type: "error",
          message: "An unexpected error occurred. Please try again later.",
        });
      });
  };

  return (
    <>
      <div className="row fw-light font-small pt-2 pb-2 mb-2 border-bottom">
        <div className="col-1">
          <img
            src={Image}
            alt="BookImage"
            style={{ width: "22px", height: "28px" }}
          />
        </div>
        <div className="col-3">{Item.title}</div>
        <div className="col-2">{Item.author}</div>
        <div className="col-2">
          {Item.borrowed ? (
            <>
              <span className="text-decoration-line-through me-1">
                ${(Item.price / 100).toFixed(2)}
              </span>
              <span className="">${(Item.finalPrice / 100).toFixed(2)}</span>
            </>
          ) : (
            `$${(Item.price / 100).toFixed(2)}`
          )}
        </div>
        <div className="col-2">
          {Item.isBorrowed ? Item.borrowDays : "Not Borrowed"}
        </div>
        <div className="col-2 d-flex justify-content-end">
          <button className=" btn-gray" onClick={handelRemoveButtonClick}>
            Remove
          </button>
        </div>
      </div>
    </>
  );
};

CartItem.propTypes = {};

export default CartItem;