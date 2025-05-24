import React from "react";
import { useNavigate } from "react-router-dom";
import AuthorImage from "../../assets/author-image.png";

const AuthorItem = ({ Item }) => {
  const navigate = useNavigate();

  const handleAuthorClick = () => {
    navigate(`/Author/${Item.name}`);
  };

  return (
    <>
      <div className="row fw-light font-small pt-2 pb-2 mb-2 border-bottom align-items-center">
        <div className="col-1 ps-0">
          <img
            src={Item.imageLink ?? AuthorImage}
            id="AuthorImage"
            alt="AuthorImage"
            style={{ width: "36px", cursor: "pointer" }}
            onClick={handleAuthorClick}
          />
        </div>
        <div
          className="col-3 ps-0"
          onClick={handleAuthorClick}
          style={{ cursor: "pointer" }}
        >
          {Item.name}
        </div>
        <div
          className="col-2 ps-0"
        >
          {Item.penName}
        </div>
        <div className="col-2 ps-0">{Item.nationality}</div>
        <div className="col-2 ps-0">{Item.born}</div>
        <div className="col-1 ps-0">{Item.died}</div>
      </div>
    </>
  );
};

export default AuthorItem;
