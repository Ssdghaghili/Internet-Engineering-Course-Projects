import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import { useNavigate } from "react-router-dom";
import Footer from "./MainCmp/Footer";
import Comment from "./MainCmp/Comment";
import Pagination from "./MainCmp/Pagination";
import cardImage from "../assets/card.png";
import AddToCartModal from "./MainCmp/AddToCartModal";
import AddReviewModal from "./MainCmp/AddReviewModal";
import Rating from "./MainCmp/Rating";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencilAlt } from "@fortawesome/free-solid-svg-icons";

const BookPage = () => {
  const navigate = useNavigate();
  const { bookSlug } = useParams();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [reviews, setReviews] = useState(null);

  useEffect(() => {
    fetch(`/api/books/${bookSlug}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        return res.json();
      })
      .then((data) => {
        setBook(data.data);
        setLoading(false);
      })
      .catch(() => {
        setError("Failed to load book details.");
        setLoading(false);
      });
  }, [bookSlug, reviews, book]);

  useEffect(() => {
    fetch(`/api/books/book/${bookSlug}/reviews`)
      .then((res) => {
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        return res.json();
      })
      .then((data) => {
        setReviews(data.data);
      })
      .catch(() => {
        setError("Failed to load reviews.");
      });
  }, [bookSlug, reviews]);

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <h3>Loading book details...</h3>
      </div>
    );
  }

  if (error) {
    return (
      <>
        <Navbar />
        <div className="container mt-5 text-center text-danger">
          <h4>{error}</h4>
        </div>
      </>
    );
  }

  const handleAuthorClick = () => {
    navigate(`/Author/${book.author}`);
  };

  const modalId = `addCartModal-${book.title.replace(/\s+/g, "-")}`;
  const checkboxId = `borrowCheckbox-${book.title.replace(/\s+/g, "-")}`;
  const daysSelectionId = `daysSelection-${book.title.replace(/\s+/g, "-")}`;
  const finalPriceId = `finalPrice-${book.title.replace(/\s+/g, "-")}`;

  return (
    <>
      <Navbar />
      <div className="container mb-5 min-vh-100">
        <div className="row mt-5 justify-content-center d-flex">
          <div className="col-10 card-booking p-0">
            <div className="row">
              <div className="col-12 col-lg-4 d-flex justify-content-center">
                <img
                  src={cardImage}
                  alt="Book"
                  className="img-fluid card-img-book"
                />
              </div>

              {/* DESKTOP DETAILS */}
              <div className="col-12 col-lg-8 mt-4 d-none d-lg-block">
                <div
                  className="clo-6 m-0 p-0 justify-content-start d-flex fw-bold"
                  style={{ fontSize: "larger" }}
                >
                  {book.title}
                </div>
                <div className="col-3 d-flex rating p-0 mb-2 mt-1 justify-content-start align-items-center">
                  <Rating Rate={book.averageRating} />
                  <span className="text-dark ms-2 text-black-50">
                    {book.averageRating}
                  </span>
                </div>
                <div className="row card-light-word">
                  <div className="col-3">Author</div>
                  <div className="col-3">Publisher</div>
                  <div className="col-2">Year</div>
                  <div className="col-3">Genre</div>
                </div>

                <div className="row mt-2">
                  <div
                    className="col-3 text-black-50"
                    onClick={handleAuthorClick}
                    style={{ cursor: "pointer" }}
                  >
                    {book.author}
                  </div>
                  <div className="col-3 text-black-50">{book.publisher}</div>
                  <div className="col-2 text-black-50">{book.year}</div>
                  <div className="col-3 text-black-50">
                    {book.genres?.join(", ")}
                  </div>
                </div>

                <div className="row card-light-word mt-2">
                  <div className="col-3">About</div>
                </div>

                <div className="row mt-2">
                  <div className="col-10 ">
                    <p>{book.synopsis}</p>
                  </div>
                </div>

                <div className="row mt-2">
                  <div className="col-2 card-book-price">
                    ${book.price / 100}
                  </div>
                </div>

                <div className="row d-flex justify-content-start mt-3">
                  <div className="col-7">
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
              </div>

              {/* Mobile Size */}

              <div className="col-12 d-lg-none mt-3 ms-md-5 ms-4">
                <div className="fw-bold" style={{ fontSize: "larger" }}>
                  {book.title}
                </div>

                <div className="d-flex align-items-center rating mt-2">
                  <Rating Rate={book.averageRating} />
                  <span className="text-dark ms-2 text-black-50">
                    {book.averageRating}
                  </span>
                </div>

                <div className="row card-light-word mt-3">
                  <div className="col-4">Author</div>
                  <div className="col-4">Publisher</div>
                  <div className="col-4">Year</div>
                </div>

                <div className="row mt-1 text-black-50">
                  <div className="col-4">{book.author}</div>
                  <div className="col-4">{book.publisher}</div>
                  <div className="col-4">{book.year}</div>
                </div>

                <div className="row card-light-word mt-3">
                  <div className="col-12">Genre</div>
                </div>

                <div className="col-12 text-black-50">
                  {book.genres?.join(", ")}
                </div>

                <div className="row card-light-word mt-3">
                  <div className="col-12">About</div>
                </div>

                <div className="row mt-1">
                  <div className="col-10 col-md-12">
                    <p>{book.synopsis}</p>
                  </div>
                </div>

                <div className="row mt-2">
                  <div className="col-6 card-book-price">
                    ${book.price / 100}
                  </div>
                </div>

                <div className="row mt-3">
                  <div className="col-12">
                    <button
                      type="button"
                      className="btn btn-add-to-cart w-75 ms-sm-4 ms-2"
                      data-bs-toggle="modal"
                      data-bs-target={`#${modalId}`}
                      onClick={(e) => e.stopPropagation()}
                    >
                      Add to Cart
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <AddToCartModal
          modalId={modalId}
          checkboxId={checkboxId}
          daysSelectionId={daysSelectionId}
          finalPriceId={finalPriceId}
          Title={book.title}
          Price={book.price / 100}
        />

        {/* Comments Section */}
        <div className="row mt-5 justify-content-center">
          <div className="col-10 card-booking p-0">
            {/* Header */}
            <div className="row mt-2">
              <div className="col-12 d-flex align-items-center justify-content-between">
                <div className="d-flex align-items-center ms-3">
                  <span>Reviews</span>
                  <span className="card-light-word ms-1">
                    {reviews == null ? 0 : reviews.length}
                  </span>
                </div>
                <button
                  type="button"
                  className="add-review-btn me-3"
                  id="openAddReviewModal"
                  data-bs-toggle="modal"
                  data-bs-target="#addReviewModal"
                >
                  <FontAwesomeIcon icon={faPencilAlt} />
                  Add Review
                </button>
              </div>
            </div>

            {/* Comments */}
            <div className="row row-cols-1 px-3">
              {reviews?.length > 0 ? (
                reviews.map((review, index) => (
                  <Comment
                    key={index}
                    Name={review.username}
                    Comment={review.comment}
                    Rate={review.rate}
                    Date={review.date}
                  />
                ))
              ) : (
                <div className="text-muted">No reviews yet.</div>
              )}
            </div>

            <Pagination />
          </div>
        </div>
      </div>

      <Footer />

      {/* Add Review Modal */}
      <AddReviewModal Book={book} Image={cardImage} />
    </>
  );
};

export default BookPage;
