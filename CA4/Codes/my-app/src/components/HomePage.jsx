import React from "react";
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

        <div className="row justify-content-center d-flex">
          <div className="col-11 d-flex justify-content-start">
            <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-5 mt-3 mb-3 gx-3 gx-md-5 gy-3 justify-content-start d-flex">
              <BookCard Title="The Shadow Hour" Author="Author 1" Price="30" Rate="2.5" To="/Book/the shadow hour"/>
              <BookCard Title="Whispers in the Dark" Author="Author 2" Price="15" Rate="3" To="/Book/Whispers in the Dark"/>
              <BookCard Title="The Lost Chronicles" Author="Author 3" Price="20" Rate="3.2" To="/Book/The Lost Chronicles"/>
              <BookCard Title="Book 4" Author="Author 4" Price="25" Rate="5"/>
              <BookCard Title="Book 5" Author="Author 5" Price="30" Rate="4.7"/>
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
              <BookCard Title="Book 6" Author="Author 6" Price="35"  />
              <BookCard Title="Book 7" Author="Author 7" Price="40" />
              <BookCard Title="Book 8" Author="Author 8" Price="45" />
              <BookCard Title="Book 9" Author="Author 9" Price="50" />
              <BookCard Title="Book 10" Author="Author 10" Price="55" />
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
