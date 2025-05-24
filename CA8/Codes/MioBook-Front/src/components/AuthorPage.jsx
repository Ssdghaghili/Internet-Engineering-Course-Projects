import { useParams } from "react-router-dom";
import React, { useEffect, useState } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import BookCard from "./MainCmp/BookCard";
import Footer from "./MainCmp/Footer";
import Comment from "./MainCmp/Comment";
import Pagination from "./MainCmp/Pagination";
import coverImage from "../assets/author-header.png";
import authorImage from "../assets/author-image.png";

const AuthorPage = () => {
  const { authorSlug } = useParams();
  const [author, setAuthor] = useState(null);
  const [authorLoading, setAuthorLoading] = useState(true);
  const [booksLoading, setBooksLoading] = useState(true);
  const [books, setBooks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAuthor = async () => {
      try {
        const res = await fetch(`/api/authors/${authorSlug}`, {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`
          },
        });
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setAuthor(data.data);
      } catch (err) {
        console.error("Failed to load Author:", err);
        setError("Failed to load Author.");
      } finally {
        setAuthorLoading(false);
      }
    };

    fetchAuthor();
  }, [authorSlug]);

  useEffect(() => {
    const fetchAuthorBooks = async () => {
      try {
        const res = await fetch(`/api/books/search?author=${authorSlug}`, {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`
          },
        });
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setBooks(data.data);
      } catch (err) {
        console.error("Failed to load Author Books:", err);
        setError("Failed to load Author Books.");
      } finally {
        setBooksLoading(false);
      }
    };

    fetchAuthorBooks();
  }, [authorSlug]);

  if (authorLoading || booksLoading) {
    return (
      <>
        <Navbar />
        <div className="container mt-5 text-center">
          <h3>Loading Author details...</h3>
        </div>
      </>
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
        {/* Desktop view */}
        <div className="d-none d-lg-block">
          <div
            className=""
            style={{
              backgroundImage: `url(${coverImage})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              height: "300px",
              width: "100%",
              position: "relative",
            }}
          >
            <div
              style={{
                position: "absolute",
                bottom: "-85px",
                left: "16%",
                transform: "translateX(-50%)",
                zIndex: 1,
                boxShadow: "0 1px 15px rgba(0, 0, 0, 0.2)",
                borderRadius: "7px",
              }}
            >
              <img
                src={authorImage}
                alt="Author"
                className="img-fluid rounded shadow"
                style={{
                  height: "320px",
                  width: "auto",
                  boxShadow: "0 1px 15px rgba(0, 0, 0, 0.2)",
                  objectFit: "cover",
                }}
              />
            </div>
          </div>

          {/* Author Details Section */}
          <div className="container-fluid mb-5">
            <div className="row mt-5 justify-content-center">
              <div className="col-10 p-0">
                <div className="row">
                  <div className="row mt-5 pt-3 text-start font-semi-samll align-items-center">
                    <h2 className="col-4 fw-bold text-start">{author.name}</h2>
                    <div className="col-1 col-xl-2 me-5"></div>
                    <div className="col-1 text-black-50 me-2">Pen Name</div>
                    <div className="col-1 text-black-50 me-2">Nationality</div>
                    <div className="col-1 text-black-50 me-2 text-center">
                      Born
                    </div>
                    {author.died && (
                      <div className="col-1 text-black-50 me-2 text-center">
                        Died
                      </div>
                    )}
                    <div className="col-1 text-black-50 text-center">Books</div>
                  </div>

                  <div className="row text-center pt-0 mt-0 font-semi-samll">
                    <h2 className="col-4 fw-bold text-start"></h2>
                    <div className="col-1 col-xl-2 me-5"></div>
                    <div className="col-1 me-2">{author.penName}</div>
                    <div className="col-1 me-2">{author.nationality}</div>
                    <div className="col-1 me-2">{author.born}</div>
                    {author.died && (
                      <div className="col-1 me-2">{author.died}</div>
                    )}
                    <div className="col-1 text-start text-center">{books.length}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Mobile View */}
        <div className="d-block d-lg-none">
          <div
            className=""
            style={{
              backgroundImage: `url(${coverImage})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              height: "300px",
              width: "100%",
              position: "relative",
            }}
          >
            <div
              style={{
                position: "absolute",
                bottom: "-85px",
                left: "50%",
                transform: "translateX(-50%)",
                zIndex: 1,
                boxShadow: "0 1px 15px rgba(0, 0, 0, 0.2)",
                borderRadius: "7px",
              }}
            >
              <img
                src={author.imageLink ?? authorImage}
                alt="Author"
                className="img-fluid rounded shadow"
                style={{
                  height: "320px",
                  width: "auto",
                  boxShadow: "0 1px 15px rgba(0, 0, 0, 0.2)",
                  objectFit: "cover",
                }}
              />
            </div>
          </div>

          {/* Author Details Section */}
          <div className="container-fluid mb-5">
            <div className="row mt-5 justify-content-center">
              <div className="col-10 p-0">
                <div className="row">
                  <div className="row mt-5 pt-3 text-center font-semi-samll align-items-center">
                    <h2 className="col-12 fw-bold ps-4">{author.name}</h2>
                  </div>
                </div>

                <div className="row mt-3">
                  <div className="col-6 text-start text-black-50 fw-light mb-1">
                    Pen Name
                  </div>
                  <div className="col-6 text-end text-black-50 fw-light mb-1">
                    Nationality
                  </div>
                  <div className="col-6 text-start">{author.penName}</div>
                  <div className="col-6 text-end">{author.nationality}</div>
                </div>

                <div className="row mt-2 ">
                  <div className="col-6 text-start text-black-50 fw-light mb-1">
                    Born
                  </div>
                  {author.died ? (
                    <div className="col-6 text-end text-black-50 fw-light mb-1">
                      Died
                    </div>
                  ) : (
                    <div className="col-6"></div>
                  )}
                  <div className="col-6 text-start">{author.born}</div>
                  {author.died ? (
                    <div className="col-6 text-end">{author.died}</div>
                  ) : (
                    <div className="col-6"></div>
                  )}
                </div>

                <div className="row mt-2 mb-1">
                  <div className="col-12 text-start text-black-50 fw-light mb-1">
                    Books
                  </div>
                  <div className="col-12 text-start">{books.length}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Books Section */}
        <div className="container mb-4">
          <div className="row justify-content-center d-flex">
            <div className="col-11 d-flex justify-content-start">
              <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 mt-3 mb-3 gx-3 gx-md-5 gy-3 justify-content-start d-flex">
                {books.map((book, index) => (
                  <BookCard
                    key={index}
                    Title={book.title}
                    Author={book.author}
                    Price={(book.price / 100).toFixed(2)}
                    Rate={book.averageRating}
                    To={`/Book/${book.title}`}
                    imageLink={book.imageLink}
                  />
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </>
  );
};
export default AuthorPage;
