import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Rating from "./Rating";

const Comment = (props) => {
  return (
    <div className="review-item">
      <div className="col py-3 d-flex align-items-center">
        
        <div className="review-avatar me-3">
          <div className="avatar-circle ">
            {props.Name.charAt(0).toUpperCase()}
          </div>
        </div>
        
        <div className="col">
          <span className="review-person">{props.Name}</span>
          <br/>
          <small className="text-muted d-md-none font-small color-gray">{props.Date}</small>
          <p className="mb-1 fw-light font-small d-md-block d-none">{props.Comment}</p>
        </div>

        <div className="rating justify-content-end d-grid me-1 text-end">
          {<Rating Rate={props.Rate} />}
          <small className="text-muted d-md-block d-none mt-1">{props.Date}</small>
        </div>
          
      </div>
      <div className="d-md-none d-block ms-5 me-5 pe-2 ">
        <p className="mb-1 fw-light font-small">{props.Comment}</p>
      </div>
     </div>
    
  );
};

Comment.propTypes = {};

export default Comment;
