import React, { useState, useEffect } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import ToastNotification from "./ToastNotification";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from "@fortawesome/free-solid-svg-icons";

const AddReviewModal = ({ Book, Image, onReviewAdded }) => {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const [comment, setComment] = useState("");

  const canSubmit = rating > 0 && comment.trim().length > 0;

  const handleSubmit = () => {
    if (localStorage.getItem("token") == null) {
      ToastNotification({
        type: "error",
        message: "Please signIn first to add a review",
      });
      return;
    }

    const reviewData = {
      title: Book.title,
      rate: rating,
      comment: comment,
    };

    fetch(`/api/review/add`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': localStorage.getItem("token"),
      },
      body: JSON.stringify(reviewData),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          ToastNotification({
            type: "success",
            message: `Review for ${Book.title} added successfully!`,
          });
          setRating(0);
          setComment("");
          if (onReviewAdded) {
            onReviewAdded();
          }
        } else {
          ToastNotification({
            type: "error",
            message: data.message || "Failed to add Review.",
            
          });
          //   console.error("Backend message:", data.message);
        }
      })
      .catch((error) => {
        console.error("Error adding Review:", error);
        ToastNotification({
          type: "error",
          message: "An unexpected error occurred. Please try again later.",
        });
      });
  };

  return (
    <div
      className="modal fade"
      id="addReviewModal"
      tabIndex="-1"
      aria-labelledby="addReviewModalLabel"
      aria-hidden="true"
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header" style={{ border: "none" }}>
            <h4
              className="modal-title fs-5 fw-medium text-center"
              id="addReviewModalLabel"
            >
              Add Review
            </h4>
            <button
              type="button"
              className="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>

          <div className="modal-body text-center">
            <img
              src={Image}
              alt="Book Cover"
              className="img-fluid rounded"
              style={{ width: "100px", height: "170px" }}
            />
            <h5 className="fw-bold mt-3">{Book.title}</h5>

            <p className="mb-1 text-start font-semi-small">Rating:</p>
            <div className="d-flex justify-content-center">
              {[1, 2, 3, 4, 5].map((value) => (
                <FontAwesomeIcon
                  key={value}
                  icon={faStar}
                  className={`star p-0 me-4 ${
                    (hoverRating || rating) >= value ? "selected" : ""
                  }`}
                  onClick={() => setRating(value)}
                  onMouseEnter={() => setHoverRating(value)}
                  onMouseLeave={() => setHoverRating(0)}
                />
              ))}
            </div>

            <p className="fw-light font-small color-dark-gray d-flex justify-content-center mt-2 me-4 mb-0">
              Tap to rate
            </p>

            <textarea
              className="add-review-textarea bg-light mt-3"
              placeholder="Type your review..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            ></textarea>
          </div>

          <div className="modal-footer" style={{ border: "none" }}>
            <button
              type="button"
              className="btn-submit vw-100 mb-2"
              id="submitReview"
              disabled={!canSubmit}
              onClick={handleSubmit}
              data-bs-dismiss="modal"
            >
              Submit Review
            </button>
            <button
              type="button"
              className="btn-cancel vw-100 m-0 mb-2"
              data-bs-dismiss="modal"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddReviewModal;
