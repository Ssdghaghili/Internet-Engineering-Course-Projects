import React, { useEffect, useState } from "react";
import { data, Link } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import ToastNotification from "../MainCmp/ToastNotification";

const AddAuthorModal = ({ ModalID, refreshAuthors }) => {
  const [name, setName] = useState("");
  const [penName, setPenName] = useState("");
  const [nationality, setNationality] = useState("");
  const [born, setBorn] = useState("");
  const [died, setDied] = useState(null);
  const [imageLink, setImageLink] = useState(null);
  const [submitError, setSubmitError] = useState("");

  const resetModal = () => {
    setName("");
    setPenName("");
    setNationality("");
    setBorn("");
    setDied(null);
    setImageLink(null);
    setSubmitError("");
  }

  const handleSubmit = async () => {

    const newAuthor = {
      name: name,
      penName: penName,
      nationality: nationality,
      born: born,
      died: died,
      imageLink: imageLink
    };

    try {
      const response = await fetch(`/api/authors/add`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          'Authorization': localStorage.getItem("token"),
        },
        body: JSON.stringify(newAuthor),
      })

      const data = await response.json();
      console.log(data.data)

      if (response.ok) {
        if (data.success) {
          ToastNotification({
            type: "success",
            message: "Author added successfully!",
          });
          refreshAuthors();
          resetModal();
        } else {
          setSubmitError(data.message)
        }
      } else {
        ToastNotification({
          type: "error",
          message: "Failed to add new author"
        });
      }
    } catch(error) {
      console.error("Error adding new author:", error);
      ToastNotification({
        type: "error",
        message: "An unexpected error occurred. Please try again later",
      })
    };
  };

  const canSubmit = (name !== "") && (penName !== "") && (nationality !== "") && (born !== null);
  const authorExists = submitError === "Author already exists";

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
                Add Author
              </h5>
            <button
              type="button"
              className="btn-close me-3"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>

          <div className="modal-body p-5 pb-0 pt-0 mt-3">
            <input
              className={ authorExists ? "modal-input bg-white rounded-3 ps-3 border-red mb-1" :
                  "modal-input bg-white rounded-3 border ps-3 mb-3"}
              placeholder="Name" value={name} onChange={e => setName(e.target.value)}>
            </input>
            {authorExists && <p className="ps-1 pt-1 input-error">Author already exists</p>}

            <input
              className="modal-input bg-white rounded-3 border ps-3"
              placeholder="Pen Name" value={penName} onChange={e => setPenName(e.target.value)}>
            </input>

            <input
              className="modal-input bg-white rounded-3 border ps-3 mt-3"
              placeholder="Nationality" value={nationality} onChange={e => setNationality(e.target.value)}>
            </input>

            <input
              className="modal-input bg-white rounded-3 border ps-3 mt-3 mb-3" type="date"
              placeholder="Born" value={born} onChange={e => setBorn(e.target.value)}>
            </input>

            <input
              className="modal-input bg-white rounded-3 border ps-3 mt-3 mb-3" type="date"
              placeholder="Died" value={died} onChange={e => setDied(e.target.value)}>
            </input>

            <input
              className="modal-input bg-white rounded-3 border ps-3 mt-3 mb-3"
              placeholder="Image Link" value={imageLink} onChange={e => setImageLink(e.target.value)}>
            </input>
          </div>

          <div
            className="modal-footer d-flex justify-content-between ms-2 me-2"
            style={{ border: "none" }}
          >
            <button
              type="button"
              className="btn-submit vw-100 mb-2"
              disabled={!canSubmit}
              onClick={handleSubmit}
            >
              Submit
            </button>
              <button
              type="button"
              className="btn-cancel vw-100 m-0 mb-2"
              data-bs-dismiss="modal"
              onClick={resetModal}
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddAuthorModal;
