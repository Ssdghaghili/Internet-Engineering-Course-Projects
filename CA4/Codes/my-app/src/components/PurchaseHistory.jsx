import React from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import Comment from "./MainCmp/Comment";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";

const PurchaseHistory = () => {
  return (
    <>
      <Navbar />
      <h1>PurchaseHistory</h1>
      <div className="container-fluid">
        <Comment
          Name="mansoor"
          Comment="xxxxxxxxxxxxxxxxxxxxxxxjbfkjbfjkdbfjksnbmb nm jhbj nmb jkbjk kjvjdtyd ufy yujd bfdslfb fjsdbfj sdbfs jfbds kjbfsldj jfbjsbfjksde feubfejkbflxxxx jjjjjjjjjjjj"
          Rate="3"
          Date="today"
        />
        
      </div>
      <Footer />
    </>
  );
};

export default PurchaseHistory;
