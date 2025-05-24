import React from "react";
import { useNavigate } from "react-router-dom";

const UserBookItem = ({ Item, Image }) => {
  const navigate = useNavigate();

  const handleBookClick = () => {
    navigate(`/Book/${Item.title}`);
  };

  const handleAuthorClick = () => {
    navigate(`/Author/${Item.author}`);
  };

    const handelReadButtonClick = () => {
        navigate(`/Book/${Item.title}/content`);
  };

  const getExpirationDate = () => {
    if (!Item.isBorrowed || !Item.borrowDays) return <br />;
    const borrowedDate = new Date();
    borrowedDate.setDate(borrowedDate.getDate() + Item.borrowDays);
    return borrowedDate.toLocaleDateString();
  };

  const expirationDate = getExpirationDate();

  return (
    <>
      <div className="row fw-light font-small pt-2 pb-2 mb-2 border-bottom">
        <div className="col-1">
          <img
            src={Image}
            alt="BookImage"
            style={{ width: "22px", height: "28px", cursor: "pointer" }}
            onClick={handleBookClick}
          />
        </div>
        <div
          className="col-2"
          onClick={handleBookClick}
          style={{ cursor: "pointer" }}
        >
          {Item.title}
        </div>
        <div
          className="col-1"
          onClick={handleAuthorClick}
          style={{ cursor: "pointer" }}
        >
          {Item.author}
        </div>
        <div className="col-2">{Item.genres?.join(", ")}</div>
        <div className="col-2">{Item.publisher}</div>
        <div className="col-1 text-center">{Item.year}</div>
        <div className="col-2 text-center">
          {!Item.isBorrowed ? "Owned" : "Borrowed"}
          {Item.isBorrowed && expirationDate && (
            <small className="d-block text-muted" style={{fontSize:"0.6rem"}}>Until: {expirationDate}</small>
          )}
        </div>
        <div className="col-1 d-flex justify-content-end" style={{height: "28px"}}>
          <button className="btn-gray" onClick={handelReadButtonClick}>
            Read
          </button>
        </div>
      </div>
    </>
  );
};

export default UserBookItem;
