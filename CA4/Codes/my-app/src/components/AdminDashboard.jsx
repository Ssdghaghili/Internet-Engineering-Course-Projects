import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";
import ToastNotification from "./MainCmp/ToastNotification";
import AdminBookItem from "./MainCmp/AdminBookItem";
import AuthorItem from "./MainCmp/AuthorItem";
import AddBookModal from "./MainCmp/AddBookModal";
import AddAuthorModal from "./MainCmp/AddAuthorModal";
import BookImage from "../assets/book-image.jpeg";
import NoBookImage from "../assets/no-history.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faBookOpen,
  faPen as faAuthor,
} from "@fortawesome/free-solid-svg-icons";
import {
  faEnvelope as faEnvelopeEmpty,
  faUser as faUserEmpty,
} from "@fortawesome/free-regular-svg-icons";

const AdminDashboard = () => {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);
  const [userLoading, setUserLoading] = useState(true);
  const [booksLoading, setBooksLoading] = useState(true);
  const [authorsLoading, setAuthorsLoading] = useState(true);
  const [books, setBooks] = useState(null);
  const [authors, setAuthors] = useState(null);

  useEffect(() => {
      const fetchUser = async () => {
        try {
          const res = await fetch(`/api/user`);
          if (!res.ok) {
            throw new Error("Network response was not ok");
          }
          const data = await res.json();
          setUser(data.data);
        } catch (err) {
          console.error("Failed to load user:", err);
          setError("Failed to load user.");
        } finally {
          setUserLoading(false);
        }
      };

      fetchUser();
    }, [user]);

  useEffect(() => {
      const fetchBooks = async () => {
        try {
          const res = await fetch(
            `/api/books/all`
          );
          if (!res.ok) {
            throw new Error("Network response was not ok");
          }
          const data = await res.json();
          setBooks(data.data);
        } catch (err) {
          console.error("Failed to load books:", err);
          setError("Failed to load books.");
        } finally {
          setBooksLoading(false);
        }
      };
      fetchBooks();
    }, [user, books]);

  useEffect(() => {
      const fetchAuthors = async () => {
        try {
          const res = await fetch(
            "/api/authors/all"
          );
          if (!res.ok) {
            throw new Error("Network response was not ok");
          }
          const data = await res.json();
          setAuthors(data.data);
        } catch (err) {
          console.error("Failed to load authors:", err);
          setError("Failed to load authors.");
        } finally {
          setAuthorsLoading(false);
        }
      };
      fetchAuthors();
    }, [user, authors]);

  const handleLogout = async () => {
      try {
        const response = await fetch("/api/logout", {
          method: "POST",
        });
        if (response.ok) {
          ToastNotification({
            type: "success",
            message: "Logout successful!",
          });

          localStorage.removeItem("user");
          // setUser(null);
          navigate("/signin");
        } else {
          console.error("Logout failed");
        }
      } catch (error) {
        console.error("Error:", error);
      }
  };


  if (userLoading || booksLoading || authorsLoading) {
      return (
        <div className="container mt-5 text-center">
          <Navbar />
          <h3>Loading details...</h3>
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
      <Navbar />

      <div className="container-lg mt-5">
        <div className="row bg-white rounded-3 p-4 align-items-center">
          <div className="col-12 col-md-10 mb-3 mb-md-0">
            <div className="d-flex flex-column gap-2">
              <div className="d-flex align-items-center mb-2">
                <FontAwesomeIcon className="pe-2" icon={faUserEmpty} />
                <span>{user.username}</span>
              </div>
              <div className="d-flex align-items-center">
                <FontAwesomeIcon className="pe-2" icon={faEnvelopeEmpty} />
                <span>{user.email}</span>
              </div>
            </div>
          </div>
          <div className="col-12 col-md-2 justify-content-start justify-content-md-end">
            <button className="btn-gray p-2 fw-medium px-4 w-100 w-md-auto" type="button" onClick={handleLogout}> Logout </button>
          </div>
        </div>

        <div className="row mt-5 mb-5 justify-content-center">
          <button
            type="button"
            className="btn btn-add-to-cart m-0 col-2"
            data-bs-toggle="modal"
            data-bs-target={`#addAuthorModal`}
            onClick={(e) => e.stopPropagation()}
          >
              Add Author
          </button>
          <div className="col-1"></div>
          <button
            type="button"
            className="btn btn-add-to-cart m-0 col-2"
            data-bs-toggle="modal"
            data-bs-target={"#addBookModal"}
            onClick={(e) => e.stopPropagation()}
          >
              Add Book
          </button>
        </div>

        <AddBookModal
          ModalID="addBookModal"
        />

        <AddAuthorModal
          ModalID="addAuthorModal"
        />

        <div className="row bg-white rounded-3 p-4 mt-5 justify-content-center align-items-center">
          <div className="col-12 show_area p-0">
            <div className="row d-flex justify-content-start">
              <div className="col-12 ms-4 ps-3">
                <h3 className="ms-4 mt-3 mb-4 text-start fw-semibold">
                  <FontAwesomeIcon className="pe-3" icon={faBookOpen} /> Books
                </h3>
              </div>
            </div>
            {books.length === 0 || !books ? ( <div className="row d-flex justify-content-center">
              <img src={NoBookImage} alt="No book" style={{ width: "350px", height: "350px" }} className="mt-2 " />
            </div> ) : ( <div className="row d-flex justify-content-center mt-3 ">
              <div className="col-11 bg-gray ps-4 pe-4 rounded-top-2">
                <div className="row color-light-black fw-light font-small pt-2 pb-2">
                  <div className="col-1 ps-0">Image</div>
                  <div className="col-2 ps-0">Name</div>
                  <div className="col-1 ps-0">Author</div>
                  <div className="col-2 ps-0">Genre</div>
                  <div className="col-2 ps-0">Publisher</div>
                  <div className="col-2 ps-0"> Published Year </div>
                  <div className="col-1 ps-0">Price</div>
                  <div className="col-1 ps-0">Total Buys</div>
                </div>
              </div>
              <div className="col-11 ps-4 pe-4 mb-4">
                {books.map((bookItem, index) => (
                <AdminBookItem key={index} Item={bookItem} /> ))}
              </div>
            </div> )}
          </div>
        </div>

        <div className="row bg-white rounded-3 p-4 mt-5 justify-content-center align-items-center">
          <div className="col-12 show_area p-0">
            <div className="row d-flex justify-content-start">
              <div className="col-12 ms-4 ps-3">
                <h3 className="ms-4 mt-3 mb-4 text-start fw-semibold">
                  <FontAwesomeIcon className="pe-3" icon={faAuthor} /> Authors
                </h3>
              </div>
            </div> {!authors || authors.length === 0 ? ( <div className="row d-flex justify-content-center">
              <img src={NoBookImage} alt="No authors" style={{ width: "350px", height: "350px" }} className="mt-2 " />
            </div> ) : ( <div className="row d-flex justify-content-center mt-3 ">
              <div className="col-11 bg-gray ps-4 pe-4 rounded-top-2">
                <div className="row color-light-black fw-light font-small pt-2 pb-2">
                  <div className="col-1 ps-0">Image</div>
                  <div className="col-3 ps-0">Name</div>
                  <div className="col-2 ps-0">Pen Name</div>
                  <div className="col-2 ps-0">Nationality</div>
                  <div className="col-2 ps-0">Born</div>
                  <div className="col-1 ps-0"> Died </div>
                </div>
              </div>
              <div className="col-11 ps-4 pe-4 mb-4"> {authors.map((author, index) => (
                <AuthorItem key={index} Item={author} /> ))}
              </div>
            </div> )}
          </div>
        </div>
      </div>

      <Footer />
    </>
  );
};

export default AdminDashboard;
