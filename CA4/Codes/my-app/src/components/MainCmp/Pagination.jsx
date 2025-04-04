import React from "react";
import PaginationNums from "./PaginationNums";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const Pagination = () => {
  return (
    <div className="row mb-0 p-0 pt-4">
      <div className="col-12 justify-content-around d-flex align-items-center">
        <nav aria-label="Page navigation example mt-5">
          <ul className="pagination justify-content-center">
            <li className="page-item">
              <a
                className="page-link pagination-link rounded-3"
                href="#"
                aria-label="Previous"
              >
                &lt;
              </a>
            </li>

            <PaginationNums page={1} selectedPage={1} />
            <PaginationNums page={2} selectedPage={1} />
            <PaginationNums page={3} selectedPage={1} />
            <PaginationNums page={4} selectedPage={1} />
            <PaginationNums page={5} selectedPage={1} />

            <li className="page-item">
              <a
                className="page-link pagination-link rounded-3"
                href="#"
                aria-label="Next"
              >
                &gt;
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  );
};

Pagination.propTypes = {};

export default Pagination;
