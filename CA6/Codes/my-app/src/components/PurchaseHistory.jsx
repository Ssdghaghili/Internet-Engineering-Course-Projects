import { useEffect, useState } from "react";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap/dist/css/bootstrap.min.css";
import NoHistory from "../assets/no-history.png";
import Navbar from "./MainCmp/Navbar";
import Footer from "./MainCmp/Footer";
import BookImage from "../assets/book-image.jpeg";
import PHistoryBody from "./MainCmp/PHistoryBody";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClockRotateLeft } from "@fortawesome/free-solid-svg-icons";

const PurchaseHistory = () => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [history, setHistory] = useState(null);
  // const [reloadFlag, setReloadFlag] = useState(false);

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const res = await fetch(
          `/api/user/purchase-history`
        );
        if (!res.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await res.json();
        setHistory(data.data);
      } catch (err) {
        console.error("Failed to load history:", err);
        setError("Failed to load history.");
      } finally {
        setLoading(false);
      }
    };
    fetchHistory();

    const handlePurchaseSuccess = () => {
      fetchHistory(); // وقتی event اومد دوباره fetch کن
    };
  
    window.addEventListener("purchaseSuccess", handlePurchaseSuccess);
  
    return () => {
      window.removeEventListener("purchaseSuccess", handlePurchaseSuccess);
    };

    
  }, []);

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <Navbar />
        <h3>Loading Purchase History details...</h3>
      </div>
    );
  }
  if (error) {
    return (
      <>
        <Navbar />
        <div className="container mt-5 text-center text-danger">
          <h4>{error}</h4>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="min-vh-100">
        <div className="row mt-5 justify-content-center bg">
          <div className="col-10 show_area p-0">
            <div className="row d-flex justify-content-start">
              <div className="col-12 ms-4 ps-3">
                <h3 className="ms-4 mt-3 mb-4 text-start fw-semibold">
                  <FontAwesomeIcon className="pe-3" icon={faClockRotateLeft} />
                  History
                </h3>
              </div>
            </div>

            {history.length === 0 ? (
            <div className="row d-flex justify-content-center">
              <img
                src={NoHistory}
                alt="No Cart"
                style={{ width: "300px", height: "300px" }}
                className="mt-2 "
              />
            </div>
            ) : (
              <PHistoryBody history={history} image={BookImage} />
             )}
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default PurchaseHistory;
