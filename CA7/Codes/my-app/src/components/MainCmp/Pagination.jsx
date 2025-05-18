import { useState } from "react";
import PaginationNums from "./PaginationNums";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Pagination({ currentPage, changeCurrentPage }) {

    const [firstPage, setFirstPage] = useState(1);

    const handlePrev = () => {
        if (firstPage > 5) {
            setFirstPage(firstPage - 5);
        }
    }

    const handleNext = () => {
        setFirstPage(firstPage + 5);
    }

    return (
        <div className="row mb-0 p-0 pt-4">
          <div className="col-12 justify-content-around d-flex align-items-center">
            <nav aria-label="Page navigation example mt-5">
              <ul className="pagination justify-content-center">
                <li className="page-item">
                  <a className="page-link pagination-link rounded-3" href="#" aria-label="Previous" onClick={handlePrev} > &lt; </a>
                </li>
                <PaginationNums page={firstPage} selectedPage={currentPage} changePage={() => {changeCurrentPage(firstPage)}} />
                <PaginationNums page={firstPage + 1} selectedPage={currentPage} changePage={() => {changeCurrentPage(firstPage + 1)}} />
                <PaginationNums page={firstPage + 2} selectedPage={currentPage} changePage={() => {changeCurrentPage(firstPage + 2)}} />
                <PaginationNums page={firstPage + 3} selectedPage={currentPage} changePage={() => {changeCurrentPage(firstPage + 3)}} />
                <PaginationNums page={firstPage + 4} selectedPage={currentPage} changePage={() => {changeCurrentPage(firstPage + 4)}} />
                <li className="page-item">
                  <a className="page-link pagination-link rounded-3" href="#" aria-label="Next" onClick={handleNext} > &gt; </a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
    );
}
