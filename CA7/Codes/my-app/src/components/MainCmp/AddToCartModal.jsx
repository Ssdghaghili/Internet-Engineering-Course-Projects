/* eslint-disable no-unused-vars */
import React, { useEffect, useRef, useState } from "react";
import { data, Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import ToastNotification from "./ToastNotification";

const AddToCartModal = ({
  modalId,
  checkboxId,
  daysSelectionId,
  finalPriceId,
  Title,
  Price,
}) => {
  const modalRef = useRef(null);
  const navigate = useNavigate();

  const handleAddButtonClick = () => {
    if (localStorage.getItem("token") == null) {
      ToastNotification({
        type: "error",
        message: "Please signIn first to add items to cart.",
      });
      return;
    }

    const root = modalRef.current;
    const borrowCheckbox = root.querySelector(".borrow-checkbox");
    const daysSelection = root.querySelector(`#${daysSelectionId}`);

    if (!borrowCheckbox.checked) {
      fetch(`/api/user/addCart?title=${Title}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          'Authorization': localStorage.getItem("token"),
        },
      })
        .then((response) => response.json())
        .then((data) => {
          if (data.success) {
            ToastNotification({
              type: "success",
              message: `${Title} added to cart successfully!`,
            });
          } else {
            ToastNotification({
              type: "error",
              message: data.message || "Failed to add book to cart.",
            });
          }
        })
        .catch((error) => {
          console.error("Error adding book to cart:", error);
          ToastNotification({
            type: "error",
            message: "An unexpected error occurred. Please try again later.",
          });
        });
    } else {
      const selectedDays = Array.from(
        daysSelection.querySelectorAll(".btn-filter-selected")
      ).map((button) => button.getAttribute("data-price"));

      if (selectedDays.length === 0) {
        ToastNotification({
          type: "error",
          message: "Please select the number of days to borrow.",
        });
        return;
      }

      const dayNumber = parseFloat(selectedDays[0]);

      fetch(`/api/user/borrow?title=${Title}&days=${dayNumber}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            'Authorization': localStorage.getItem("token")
          },
        }
      )
        .then((response) => response.json())
        .then((data) => {
          if (data.success) {
            ToastNotification({
              type: "success",
              message: `${Title} added to cart successfully!(browwing for ${dayNumber} days)`,
            });
          } else {
            ToastNotification({
              type: "error",
              message: data.message || "Failed to add book to cart.",
            });
            // console.error("Backend message:", data.message);
          }
        })
        .catch((error) => {
          console.error("Error adding book to cart:", error);
          ToastNotification({
            type: "error",
            message: "An unexpected error occurred. Please try again later.",
          });
        });
    }
  };


  useEffect(() => {
    const root = modalRef.current;
    const borrowCheckbox = root.querySelector(".borrow-checkbox");
    const daysSelection = root.querySelector(`#${daysSelectionId}`);
    const finalPrice = root.querySelector(`#${finalPriceId}`);

    const handleCheckboxChange = function () {
      if (this.checked) {
        daysSelection.classList.remove("d-none");
        finalPrice.textContent = "$0.00";
      } else {
        daysSelection.classList.add("d-none");
        finalPrice.textContent = `$${Price}`;
      }
    };

    const handleDayButtonClick = function () {
      const borrowDay = this.getAttribute("data-price");
      const borrowDayPrice = parseFloat(borrowDay);
      const bookPrice = parseFloat(Price);

      if (borrowDayPrice > 0 && bookPrice > 0) {
        finalPrice.textContent = `$${(
          (borrowDayPrice * bookPrice) /
          10
        ).toFixed(2)}`;
      } else {
        console.error("Invalid day or price");
        finalPrice.textContent = "$0.00";
      }

      root.querySelectorAll(".btn-filter").forEach((btn) => {
        btn.classList.remove("btn-filter-selected");
      });
      this.classList.add("btn-filter-selected");
    };

    if (borrowCheckbox) {
      borrowCheckbox.addEventListener("change", handleCheckboxChange);
    }

    const dayButtons = root.querySelectorAll(".btn-filter");
    dayButtons.forEach((button) => {
      button.addEventListener("click", handleDayButtonClick);
    });

    const modalEl = document.getElementById(modalId);
    const handleHidden = () => {
      document.getElementById("focusAfterModal")?.focus();
    };

    modalEl?.addEventListener("hidden.bs.modal", handleHidden);

    return () => {
      if (borrowCheckbox) {
        borrowCheckbox.removeEventListener("change", handleCheckboxChange);
      }
      dayButtons.forEach((button) => {
        button.removeEventListener("click", handleDayButtonClick);
      });

      modalEl?.removeEventListener("hidden.bs.modal", handleHidden);
    };
  }, [Price, modalId, daysSelectionId, finalPriceId]);

  return (
    <div
      className="modal fade"
      id={modalId}
      tabIndex="-1"
      aria-labelledby={`${modalId}-label`}
      aria-hidden="true"
      ref={modalRef}
    >
      <div className="modal-dialog modal-title modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header position-relative font-semi-samll">
            <h5
              className="modal-title fs-5 fw-medium text-center ms-2"
              id={`${modalId}-label`}
            >
              Add to Cart
            </h5>
            <button
              type="button"
              className="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div className="modal-body">
            <p className="fw-light ms-2 font-semi-samll">
              Would you like to buy or borrow this book?
            </p>

            <div className="form-check ms-2">
              <input
                className="form-check-input borrow-checkbox"
                type="checkbox"
                id={checkboxId}
              />
              <label
                className="form-check-label fw-light font-semi-samll"
                htmlFor={checkboxId}
              >
                Borrow this book
              </label>
            </div>

            <div
              id={daysSelectionId}
              className="mt-3 ms-2 me-2 d-none row row-cols-3 justify-content-center"
            >
              {[...Array(9)].map((_, i) => (
                <div className="col" key={i}>
                  <button
                    className="btn btn-filter w-100 h-50"
                    data-price={i + 1}
                  >
                    {i + 1} Day{i > 0 && "s"}
                  </button>
                </div>
              ))}
            </div>
          </div>

          <div
            className="modal-footer d-flex justify-content-between ms-2 me-2"
            style={{ border: "none" }}
          >
            <p className="fw-light font-semi-samll">
              Final Price:{" "}
              <span
                id={finalPriceId}
                className="fw-bold p-0 m-0 font-semi-samll"
              >
                ${Price}
              </span>
            </p>
            <button
              type="button"
              className="btn-main border-0 m-0 mb-2"
              style={{ width: "24%", height: "30px" }}
              data-bs-dismiss="modal"
              onClick={handleAddButtonClick}
            >
              Add
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddToCartModal;
