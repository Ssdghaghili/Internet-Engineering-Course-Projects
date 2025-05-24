import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar, faStarHalfAlt } from "@fortawesome/free-solid-svg-icons";
import { faStar as faStarEmpty } from "@fortawesome/free-regular-svg-icons";

const Rating = (props) => {
    const rate = parseFloat(props.Rate);
    const stars = [];

    for (let i = 0; i < 5; i++) {
        if (i+1 <= rate) {
            stars.push(<FontAwesomeIcon key={i} icon={faStar} className="p-0" />);
        } else if (i < rate) {
            stars.push(<FontAwesomeIcon key={i} icon={faStarHalfAlt} className="p-0" />);
        } else {
            stars.push(<FontAwesomeIcon key={i} icon={faStarEmpty} className="p-0" />);
        }
    }

    return <div>{stars}</div>;
};

export default Rating;
