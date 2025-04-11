import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import HistoryItem from "./HistoryItem";


const PHistoryBody = ({ history, image }) => {
  return (
    <>
      <div
        className="accordion ms-4 me-4 mb-4 mt-4"
        id="purchaseHistoryAccordion"
      >
        {history.map((entry, index) => (
          <div className="accordion-item font-semi-small" key={index}>
            <p className="accordion-header" id={`heading-${index}`}>
              <div className="bg-gray">
                <button
                  className="accordion-button collapsed bg-custom-gray"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target={`#collapse-${index}`}
                  aria-expanded="false"
                  aria-controls={`collapse-${index}`}
                >
                  <p className="m-0 font-semi-samll fw-light">
                    {entry.purchaseDate} &nbsp; | &nbsp; ${entry.totalCost}
                  </p>
                </button>
              </div>
            </p>
            <div
              id={`collapse-${index}`}
              className="accordion-collapse collapse"
              aria-labelledby={`heading-${index}`}
              data-bs-parent="#purchaseHistoryAccordion"
            >
              <div className="accordion-body">
                <div className="row d-flex justify-content-center mt-3 ">
                  <div className="col-11 bg-gray ps-4 pe-4 rounded-top-2">
                    <div className="row color-light-black fw-light font-small pt-2 pb-2">
                      <div className="col-1">Image</div>
                      <div className="col-3">Name</div>
                      <div className="col-3 ps-2">Author</div>
                      <div className="col-2 ps-0">Price</div>
                      <div className="col-3 ps-0">Borrow Days</div>
                    </div>
                  </div>

                  <div className="col-11 ps-4 pe-4  mb-1">
                    <div className="row fw-light font-small pt-2 pb-2">
                      {entry.items.map((item, i) => (
                        <HistoryItem key={i} Item={item} Image={image} />
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </>
  );
};

PHistoryBody.propTypes = {};

export default PHistoryBody;
