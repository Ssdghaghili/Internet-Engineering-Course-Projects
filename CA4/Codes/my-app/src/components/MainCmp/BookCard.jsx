import React, { useEffect, useRef } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import cardImage from "../../assets/card.png";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import Rating from "./Rating";
import AddToCartModal from "./AddToCartModal";

const BookCard = (props) => {
  const cardRef = useRef(null);
  const navigate = useNavigate();
  const modalId = `addCartModal-${props.Title.replace(/\s+/g, "-")}`;
  const checkboxId = `borrowCheckbox-${props.Title.replace(/\s+/g, "-")}`;
  const daysSelectionId = `daysSelection-${props.Title.replace(/\s+/g, "-")}`;
  const finalPriceId = `finalPrice-${props.Title.replace(/\s+/g, "-")}`;

  const handelCardClick = () => {
    navigate(`/Book/${props.Title}`);
  };

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

      // console.log("Final price:", finalPrice.textContent);

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
      <div className="card card-preview" onClick={handelCardClick}>
        <img src={props.imageLink ?? cardImage} className="card-img-top" alt="..." />
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
            onClick={(e) => e.stopPropagation()}
          >
            Add to Cart
          </button>
        </div>
      </div>

      {/* <button id="focusAfterModal" style={{ display: "none" }} /> */}

      <AddToCartModal
        modalId={modalId}
        checkboxId={checkboxId}
        daysSelectionId={daysSelectionId}
        finalPriceId={finalPriceId}
        Title={props.Title}
        Price={props.Price}
      />
    </div>
  );
};

export default BookCard;
