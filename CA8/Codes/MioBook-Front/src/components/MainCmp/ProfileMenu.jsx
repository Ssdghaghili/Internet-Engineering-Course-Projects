import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const ProfileMenu = (props) => {
  return (
    <a onClick={props.onClick}>
      <FontAwesomeIcon className="pe-1" icon={props.Icon} />
      {props.Title}
    </a>
  );
};

export default ProfileMenu;
