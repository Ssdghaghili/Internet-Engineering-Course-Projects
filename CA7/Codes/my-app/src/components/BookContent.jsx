import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBook } from "@fortawesome/free-solid-svg-icons";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";

const BookContent = () => {
  const { bookSlug } = useParams();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBookContent = async () => {
      try {
        const res = await fetch(`/api/books/book/${bookSlug}/content`, {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`
          },
        });
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setBook(data.data);
      } catch (err) {
        console.error("Failed to load book content:", err);
        setError("Failed to load book content.");
      } finally {
        setLoading(false);
      }
    };
    fetchBookContent();
  }, [bookSlug]);

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <h3>Loading book content...</h3>
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

  return (
    <>
      <Navbar />
      <div className="min-vh-100">
        <div className="container mb-5">
          <div className="row mt-5 card-booking">
            <div className="col p-0 d-flex align-items-center justify-content-between">
              <p className="fw-semibold ms-3 mt-3 mb-3 fs-4">
                <FontAwesomeIcon className="mt-3 me-2" icon={faBook} />
                {book.title}
              </p>

              <p className="fw-light me-4 mb-0">By {book.author.name}</p>
            </div>
          </div>
        </div>

        <div className="container mb-5">
          <div className="row mt-5 card-booking p-4">
            <p className="" style={{ fontSize: "1.1rem" }}>
              {book.content}
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default BookContent;
