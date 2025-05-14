import React, { useEffect, useRef, useState } from "react";
import { data, Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import ToastNotification from "./ToastNotification";

const AddBookModal = ({ ModalID, refreshBooks }) => {
  const [step, setStep] = useState(1);

  const [name, setName] = useState("");
  const [author, setAuthor] = useState("");
  const [publisher, setPublisher] = useState("");
  const [genres, setGenres] = useState("");
  const [publishedYear, setPublishedYear] = useState(null);
  const [price, setPrice] = useState(null);
  const [imageLink, setImageLink] = useState(null);
  const [synopsis, setSynopsis] = useState("");
  const [content, setContent] = useState("");

  const [submitError, setSubmitError] = useState(null);

  const resetModal = () => {
    setName("");
    setAuthor("");
    setPublisher("");
    setGenres("");
    setPublishedYear(null);
    setPrice(null);
    setImageLink(null);
    setSynopsis("");
    setContent("");
    setSubmitError(null);
  }

  const handleSubmit = async () => {

    const newBook = {
      title: name,
      author: author,
      publisher: publisher,
      year: publishedYear,
      genres: genres.split(" "),
      price: Math.round(parseFloat(price) * 100),
      imageLink: imageLink,
      synopsis: synopsis,
      content: content
    };

    try {
      const response = await fetch("/api/books/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          'Authorization': localStorage.getItem("token"),
        },
        body: JSON.stringify(newBook),
      })

      const data = await response.json();

      if (response.ok) {
        if (data.success) {
            console.log(data);
          ToastNotification({
            type: "success",
            message: "Book added successfully!",
          });
          refreshBooks();
          resetModal();
        } else {
          setSubmitError(data.message)
        }
      } else {
        ToastNotification({
          type: "error",
          message: "Failed to add new book"
        });
      }
    } catch(error) {
      console.error("Error adding new book:", error);
      ToastNotification({
        type: "error",
        message: "An unexpected error occurred. Please try again later",
      })
    }
  };

  const canSubmit = (name !== "") && (author !== "") && (publisher !== "") && (genres !== "") &&
    (synopsis !== "") && (content !== "") && (price > 0) && (publishedYear > 0);
  const bookTitleExists = submitError === "Book already exists";
  const authorDoesNotExist = submitError === "Author not found";

  return (
    <div
      className="modal fade"
      id={ModalID}
      tabIndex="-1"
      aria-labelledby={`${ModalID}-label`}
      aria-hidden="true"
    >
      <div className="modal-dialog modal-title modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header position-relative font-semi-small border-0 mt-3">
            <h5
                className="modal-title fs-5 fw-medium position-absolute start-50 translate-middle-x"
                id={`${ModalID}-label`}
              >
                Add Book
              </h5>
            <button
              type="button"
              className="btn-close me-3"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>

          <div className="modal-body p-5 pb-0 pt-0 mt-3">
            { step === 1 && (

              <>
              <input
                className={ bookTitleExists ? "modal-input bg-white rounded-3 ps-3 border-red mb-1" :
                                  "modal-input bg-white rounded-3 border ps-3 mb-3"}
                placeholder="Name" value={name} onChange={e => setName(e.target.value)}>
              </input>
              {bookTitleExists && <p className="ps-1 pt-1 input-error">Book title already exists</p>}

              <input
                className={ authorDoesNotExist ? "modal-input bg-white rounded-3 ps-3 border-red mb-1" :
                                                  "modal-input bg-white rounded-3 border ps-3 mb-3"}
                placeholder="Author" value={author} onChange={e => setAuthor(e.target.value)}>
              </input>
              {authorDoesNotExist && <p className="ps-1 pt-1 input-error">Author doesn't exists</p>}

              <input
                className="modal-input bg-white rounded-3 border ps-3"
                placeholder="Publisher" value={publisher} onChange={e => setPublisher(e.target.value)}>
              </input>

              <input
                className="modal-input bg-white rounded-3 border ps-3 mt-3"
                placeholder="Genres" value={genres} onChange={e => setGenres(e.target.value)}>
              </input>

              <input
                className="modal-input bg-white rounded-3 border ps-3 mt-3" type="number"
                placeholder="Published Year" value={publishedYear} onChange={e => setPublishedYear(e.target.value)}>
              </input>

              <input
                className="modal-input bg-white rounded-3 border ps-3 mt-3" type="number"
                placeholder="Price" value={price} onChange={e => setPrice(e.target.value)}>
              </input>

              <input
                className="modal-input bg-white rounded-3 border ps-3 mt-3 mb-3"
                placeholder="Image Link" value={imageLink} onChange={e => setImageLink(e.target.value)}>
              </input>
              </>
            )}

            { step === 2 && (
              <textarea
                className="add-synopsis-textarea bg-white rounded-3 border ps-3 pt-2"
                placeholder="Synopsis"
                value={synopsis}
                onChange={(e) => setSynopsis(e.target.value)}
              ></textarea>
            )}

            { step === 2 && (
              <textarea
                className="add-Content-textarea bg-white rounded-3 border ps-3 pt-2 mt-3 mb-3"
                placeholder="Content"
                value={content}
                onChange={(e) => setContent(e.target.value)}
              ></textarea>
            )}
          </div>

          <div
            className="modal-footer d-flex justify-content-between ms-2 me-2"
            style={{ border: "none" }}
          >
            { step === 1 && (
              <button
                type="button"
                className="btn-submit vw-100 mb-2"
                onClick={() => setStep(2)}
              >
                Next
              </button>
            )}
            { step === 1 && (
              <button
                type="button"
                className="btn-cancel vw-100 m-0 mb-2"
                data-bs-dismiss="modal"
              >
                Cancel
              </button>
            )}

            { step === 2 && (
              <button
                type="button"
                className="btn-submit vw-100 mb-2"
                id="nextStep"
                disabled={!canSubmit}
                onClick={handleSubmit}
              >
                Submit
              </button>
            )}
            { step === 2 && (
              <button
                type="button"
                className="btn-cancel vw-100 m-0 mb-2"
                onClick={() => setStep(1)}
              >
                Back
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddBookModal;
