import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFilter } from "@fortawesome/free-solid-svg-icons";
import Navbar from "./MainCmp/Navbar"
import BookCard from "./MainCmp/BookCard"
import Pagination from "./MainCmp/Pagination"
import ToastNotification from "./MainCmp/ToastNotification"
import Footer from "./MainCmp/Footer"


export default function SearchResult() {
    const genres = ["fiction", "non-fiction", "science", "history", "fantasy", "biography", "mystery", "romance"];
    const Title = "What?"

    const [searchParams] = useSearchParams();
    useEffect(() => {
        fetchBooks(searchParams);
    }, [searchParams])

    const [lastFilters, setLastFilters] = useState();

    const [page, setPage] = useState(1);
    function changePage(newPage) {
        setPage(newPage);
        fetchBooks({...lastFilters, page: newPage});
    }

    const [sortBy, setSortBy] = useState("");
    function handleSortBy(s) {
        setSortBy(s === sortBy ? "" : s);
    }

    const [order, setOrder] = useState("");
    function handleOrder(o) {
        setOrder(o === order ? "" : o);
    }

    const [books, setBooks] = useState([]);

    const fetchBooks = async (filters) => {
        try {
            const query = new URLSearchParams(filters).toString();
            const url = `http://localhost:8080/api/books/search?${query}`;

            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const res = await response.json();

            ToastNotification({ type: (res.status === "OK") ? "success" : "error", message: res.message });

            setBooks(res.data);

        } catch (err) {
            ToastNotification({ type: "error", message: err.message });
        }
    }

    const applyFilter = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const formFilters = Object.fromEntries(formData.entries().filter(([_, val]) => val !== ""));
        const filters = {...formFilters, ...(sortBy ? { sortBy } : {}), ...(order ? { order } : {})};
        setLastFilters(filters);
        setPage(1);
        fetchBooks(filters);
    }

    return (
        <>
        <Navbar />

        <div className="container mb-5">
          <div className="row mt-5 justify-content-center align-items-center">
            <div className="col-12 col-md-6 h5 fw-semibold text-md-start text-center"> Results for {Title} </div>
            <div className="col-12 col-md-6 d-flex justify-content-end me-5 me-md-0">
              <div className="col-12 col-md-5 d-flex justify-content-end me-5 me-md-0">
                <a className="btn btn-main fw-medium p-2" style={{ width: 120 }} data-bs-toggle="offcanvas" href="#FilterOffcanvas" role="button" aria-controls="FilterOffcanvas">
                  <FontAwesomeIcon icon={faFilter} className="pe-1" /> Filter
                </a>
              </div>
            </div>
          </div>

          <div className="offcanvas offcanvas-start bg-light" tabIndex={-1} id="FilterOffcanvas" aria-labelledby="FilterOffcanvasLabel">
            <div className="offcanvas-header">
              <h5 className="offcanvas-title fw-bold" id="FilterOffcanvasLabel" />
              <button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close" />
            </div>

            <div className="offcanvas-body" style={{ border: "none" }}>
              <div class="row">
                <div className="col justify-content-center text-center font-semi-big fw-bold mb-3"> Filters </div>
                <hr className="my-3 mb-4" />
              </div>
              <form onSubmit={applyFilter}>
                <div className="mb-3 d-flex align-items-center">
                  <label htmlFor="title" className="form-label fw-light me-3" style={{ minWidth: 120 }}> Book Title: </label>
                  <input type="text" className="form-control form-filter fw-light flex-grow-1" name="title" placeholder="book title" />
                </div>
                <div className="mb-3 d-flex align-items-center">
                  <label htmlFor="author" className="form-label fw-light me-3" style={{ minWidth: 120 }}> Author Name: </label>
                  <input type="text" className="form-control form-filter fw-light flex-grow-1" name="author" placeholder="author name" />
                </div>
                <div className="mb-3 d-flex align-items-center">
                  <label htmlFor="genre" className="form-label fw-light me-3" style={{ minWidth: 120 }}> Genre: </label>
                  <select className="form-select form-select-filter fw-light flex-grow-1" name="genre" >
                        <option key="none" value="">Select a genre</option>
                    {genres.map((genre) => (
                        <option key={genre} value={genre}>{genre}</option>
                      ))}
                  </select>
                </div>
                <div className="mb-3 d-flex align-items-center">
                  <label htmlFor="publishedYear" className="form-label fw-light me-3" style={{ minWidth: 120 }}> Published Year: </label>
                  <input type="number" className="form-control form-filter fw-light flex-grow-1" name="year" placeholder="Year" />
                </div>
                <div className="mb-3 d-flex align-items-center">
                  <label className="form-label fw-light me-3" style={{ minWidth: 120 }}> Sort By: </label>
                  <div className="d-flex gap-2 flex-grow-1">
                    <button type="button" className={`btn btn-filter flex-grow-1 ${sortBy === "rating" ? "active" : "" }`} onClick={() => handleSortBy("rating")}> Rating </button>
                    <button type="button" className={`btn btn-filter flex-grow-1 ${sortBy === "reviews" ? "active" : "" }`} onClick={() => handleSortBy("reviews")}> Reviews </button>
                  </div>
                </div>
                <div className="mb-3 d-flex align-items-center">
                  <label className="form-label fw-light me-3" style={{ minWidth: 120 }}> Order: </label>
                  <div className="d-flex gap-2 flex-grow-1">
                    <button type="button" className={`btn btn-filter flex-grow-1 ${order === "desc" ? "active" : "" }`} onClick={() => handleOrder("desc")}> Descending </button>
                    <button type="button" className={`btn btn-filter flex-grow-1 ${order === "asc" ? "active" : "" }`} onClick={() => handleOrder("asc")}> Ascending </button>
                  </div>
                </div>
                <div className="position-absolute bottom-0 start-0 w-100 p-3 bg-white bg-light">
                  <button type="submit" className="btn btn-main fw-light fs-6 w-100" > Apply </button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div className="container mb-4">
          <div className="row justify-content-center d-flex">
            <div className="col-11 d-flex justify-content-start">
              <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 mt-3 mb-3 gx-3 gx-md-5 gy-3 justify-content-start d-flex">
                {books.map((book) => (
                    <BookCard Title={book.title} Author={book.author} Price={book.price} Rate={book.averageRating} To="/Book/the shadow hour" key={book.title} />
                ))}
              </div>
            </div>
          </div>
        </div>

        <Pagination currentPage={page} changeCurrentPage={changePage} />

        <Footer />
        </>
    );
}