import React, { useEffect, useRef } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import cardImage from "../../assets/card.png";
import Rating from "./Rating";

const BookCard = (props) => {
  
  const cardRef = useRef(null);
  const modalId = `addCartModal-${props.Title.replace(/\s+/g, '-')}`;
  const checkboxId = `borrowCheckbox-${props.Title.replace(/\s+/g, '-')}`;
  const daysSelectionId = `daysSelection-${props.Title.replace(/\s+/g, '-')}`;
  const finalPriceId = `finalPrice-${props.Title.replace(/\s+/g, '-')}`;



  console.log("BookCard props:", props);
  

  useEffect(() => {
    
    const root = cardRef.current;

    const borrowCheckbox = root.querySelector(".borrow-checkbox");
    const daysSelection = root.querySelector(`#${daysSelectionId}`);
    const finalPrice = root.querySelector(`#${finalPriceId}`);

    const handleCheckboxChange = function () {
      if (this.checked) {
        daysSelection.classList.remove("d-none");
        finalPrice.textContent = "$0.00";
      } else {
        daysSelection.classList.add("d-none");
        finalPrice.textContent = `$${props.Price}`;
      }
    };

    const handleDayButtonClick = function () {
      const borrowDay = this.getAttribute("data-price");
      const borrowDayPrice = parseFloat(borrowDay);
      const bookPrice = parseFloat(props.Price);

      if (borrowDayPrice > 0 && bookPrice > 0) {
        finalPrice.textContent = `$${(
          (borrowDayPrice * bookPrice) /
          10
        ).toFixed(2)}`;
      } else {
        console.error("Error: Invalid borrowDayPrice or bookPrice");
        finalPrice.textContent = "$0.00";
      }

      console.log("Final price:", finalPrice.textContent);

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

    return () => {
      if (borrowCheckbox) {
        borrowCheckbox.removeEventListener("change", handleCheckboxChange);
      }
      dayButtons.forEach((button) => {
        button.removeEventListener("click", handleDayButtonClick);
      });
    };
  });

  return (
    
    <div className="col col-8 mx-auto" ref={cardRef}>
      <div className="card card-preview">
        <img src={cardImage} className="card-img-top" alt="..." />
        <div className="card-body">
          <h5
            className="card-title text-center fw-bold mb-1 mt-2"
            style={{ fontSize: "1rem" }}
          >
            {props.Title}
          </h5>
          <p
            className="card-text text-center fw-light font-semi-samll"
            style={{ color: "#454545" }}
          >
            {props.Author}
          </p>
          <div className="row d-flex m-0 p-0">
            <div className="col-6 d-flex rating p-0 mb-2 justify-content-start align-items-center">
            
              <Rating Rate={props.Rate} />
            
            </div>
            <div className="col-6 d-flex justify-content-end p-0 m-0">
              <p className="price fw-light justify-content-end p-0 m-0 mb-2">
                ${props.Price}
              </p>
            </div>
          </div>
          <button
            type="button"
            className="btn btn-add-to-cart m-0"
            data-bs-toggle="modal"
            data-bs-target={`#${modalId}`}
          >
            Add to Cart
          </button>
        </div>
      </div>

      <div
        className="modal fade"
        id={modalId}
        tabIndex="-1"
        aria-labelledby={`${modalId}-label`}
        aria-hidden="true"
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
                />
                <label
                  className="form-check-label fw-light font-semi-samll"
                  htmlFor={checkboxId}
                >
                  Borrow this book
                </label>
              </div>

              <div id={daysSelectionId} className="mt-3 ms-2 me-2 d-none row row-cols-3 justify-content-center">
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="1">
                    1 Day
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="2">
                    2 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="3">
                    3 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="4">
                    4 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="5">
                    5 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="6">
                    6 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="7">
                    7 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="8">
                    8 Days
                  </button>
                </div>
                <div className="col">
                  <button className="btn btn-filter w-100 h-50" data-price="9">
                    9 Days
                  </button>
                </div>
              </div>
            </div>

            <div
              className="modal-footer d-flex justify-content-between ms-2 me-2"
              style={{ border: "none" }}
            >
              <p className="fw-light font-semi-samll">
                Final Price:
                <span  id={finalPriceId} className="fw-bold p-0 m-0 font-semi-samll">
                  ${props.Price}
                </span>
              </p>
              <button
                type="button"
                className="btn-main border-0 m-0 mb-2"
                style={{ width: "24%", height: "30px" }}
                id="submitReview"
                data-bs-dismiss="modal"
              >
                Add
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};


export default BookCard;
