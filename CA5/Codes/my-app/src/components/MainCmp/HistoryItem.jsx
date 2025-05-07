import React from "react";

const HistoryItem = ({ Item, Image }) => {
  // console.log(Item);
  const { book } = Item;
  return (
    <>
      <div className="row fw-light font-small pt-2 pb-2 mb-2 border-bottom">
        <div className="col-1">
          <img
            src={Image}
            alt="BookImage"
            style={{ width: "22px", height: "28px" }}
          />
        </div>
        <div className="col-3">{book.title}</div>
        <div className="col-3">{book.author}</div>
        <div className="col-2">
          {Item.borrowed ? (
            <>
              <span className="text-decoration-line-through me-1">
                ${(book.price / 100).toFixed(2)}
              </span>
              <span className="">${(Item.finalPrice / 100).toFixed(2)}</span>
            </>
          ) : (
            `$${(book.price / 100).toFixed(2)}`
          )}
        </div>
        <div className="col-3 ps-3">
          {Item.borrowed ? Item.borrowDays : "Not Borrowed"}
        </div>
      </div>
    </>
  );
};


export default HistoryItem;