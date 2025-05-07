import React from "react";
import { useEffect, useState } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";
import BookCard from "./MainCmp/BookCard";
import DescriptionText from "./HomePageCmp/DescriptionText";
import LibraryImage from "./HomePageCmp/LibraryImage";
import HomeSearch from "./HomePageCmp/HomeSearch";
import { ToastContainer } from "react-toastify";


const HomePage = () => {

  const [newBooksLoading, setNewBooksLoading] = useState(true);
  const [topBooksLoading, setTopBooksLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newBooks, setNewBooks] = useState([]);
  const [topBooks, setTopBooks] = useState([]);

  useEffect(() => {
    const fetchNewReleaseBooks = async () => {
      try {
        const res = await fetch(`/api/books/book/new-releases?size=5`);
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setNewBooks(data.data);
      } catch (err) {
        console.error("Failed to load New Books:", err);
        setError("Failed to load New Books.");
      } finally {
        setNewBooksLoading(false);
      }
    };

    fetchNewReleaseBooks();
  }, []);


  useEffect(() => {
    const fetchTopRatedBooks = async () => {
      try {
        const res = await fetch(`/api/books/book/top-rated?size=5`);
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setTopBooks(data.data);
      } catch (err) {
        console.error("Failed to load Top Books:", err);
        setError("Failed to load Top Books.");
      } finally {
        setTopBooksLoading(false);
      }
    };

    fetchTopRatedBooks();
  }, []);

  if (newBooksLoading || topBooksLoading) {
    return (
      <div className="container mt-5 text-center">
        <Navbar />
        <h3>Loading books ...</h3>
      </div>
    );
  }
  if (error) {
    return (
      <>
        <Navbar />
        <div className="container mt-5 text-center">
          <h3>{error}</h3>
        </div>
      </>
    );
  }

  return (
    <>
      <ToastContainer />
      <Navbar />

      <div className="container d-md-none">
        <div className="col-12 fw-light fs-6 mt-3 ms-3">Easy Search...</div>

        <div className="col-12 mt-3 me-3">
          <HomeSearch />
        </div>
      </div>

      <div className="container-fluid">
        <div className="row justify-content-center bg-beige mt-3 mt-md-5 rounded-3">
          <div className="col-9">
            <div className="row">
              <div className="col-12 d-none d-lg-block col-lg-5 mt-3 p-3 mb-4">
                <DescriptionText />
              </div>

              <div className="col-12 d-block d-lg-none d-flex justify-content-center mt-3 p-3 mb-4">
                <LibraryImage />
              </div>

              {/* mobile size */}
              <div className="col-12 d-none d-lg-block col-lg-7 d-lg-flex justify-content-end mt-3">
                <LibraryImage width="60%" height="60%" />
              </div>

              <div className="col-12 d-block d-lg-none d-flex justify-content-center m-0 p-0">
                <DescriptionText />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="container mb-4">
        <div className="row mt-5 justify-content-center">
          <div className="col-11 justify-content-start font-semi-big fw-light">
            New Releases
          </div>
        </div>

        <div className="row justify-content-center">
          <div className="col-11 d-flex justify-content-start">
            <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 mt-3 mb-3 gx-3 gx-md-5 gy-3 justify-content-start">
              {newBooks.map((book, index) => (
                <BookCard
                  key={index}
                  Title={book.title}
                  Author={book.author}
                  Price={(book.price/100).toFixed(2)}
                  Rate={book.averageRating}
                  To={`/Book/${book.title}`}
                  imageLink={book.imageLink}
                />
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="container mb-5">
        <div className="row mt-2 justify-content-center">
          <div className="col-11 justify-content-start font-semi-big fw-light">
            Top Rated
          </div>
        </div>

        <div className="row justify-content-center d-flex">
          <div className="col-11 d-flex justify-content-start">
            <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 mt-3 mb-3 gx-3 gx-md-5 gy-3 justify-content-start d-flex">
              {topBooks.map((book, index) => (
                <BookCard
                  key={index}
                  Title={book.title}
                  Author={book.author}
                  Price={(book.price/100).toFixed(2)}
                  Rate={book.averageRating}
                  To={`/Book/${book.title}`}
                  imageLink={book.imageLink}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

HomePage.propTypes = {};

export default HomePage;
