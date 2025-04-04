import React from 'react'
import libraryImage from "../../assets/Libary.png";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";

const LibraryImage = ({ width = "100%", height = "100%" }) => (
    <img src={libraryImage} alt="cover" className="img-fluid" width={width} height={height} />
  );

export default LibraryImage