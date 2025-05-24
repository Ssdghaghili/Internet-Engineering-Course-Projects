import React from "react";
import { useNavigate } from "react-router-dom";
import BookImage from "../../assets/book-image.jpeg";

const AdminBookItem = ({ Item }) => {
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
      <div className="row fw-light font-small pt-2 pb-2 mb-2 border-bottom align-items-center">
        <div className="col-1 ps-0">
          <img
            src={Item.imageLink ?? BookImage}
            id="BookImage"
            alt="BookImage"
            style={{ width: "36px", cursor: "pointer" }}
            onClick={handleBookClick}
          />
        </div>
        <div
          className="col-2 ps-0"
          onClick={handleBookClick}
          style={{ cursor: "pointer" }}
        >
          {Item.title}
        </div>
        <div
          className="col-1 ps-0"
          onClick={handleAuthorClick}
          style={{ cursor: "pointer" }}
        >
          {Item.author}
        </div>
        <div className="col-2 ps-0">{Item.genres?.join(", ")}</div>
        <div className="col-2 ps-0">{Item.publisher}</div>
        <div className="col-2 ps-0">{Item.year}</div>
        <div className="col-1 ps-0">${(Item.price/100).toFixed(2)}</div>
        <div className="col-1 ps-0">{Item.totalBuys}</div>
      </div>
    </>
  );
};

export default AdminBookItem;
