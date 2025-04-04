import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Slide } from "react-toastify";
import PropTypes from "prop-types";

const ToastNotification = ({ type = "success", message = "Notification!" }) => {
  const showToast = () => {
    if (type === "success") {
      toast.success(message, toastConfig);
    } else if (type === "error") {
      toast.error(message, toastConfig);
    } else if (type === "info") {
      toast.info(message, toastConfig);
    } else if (type === "warning") {
      toast.warning(message, toastConfig);
    } else {
      toast(message, toastConfig);
    }
  };

  const toastConfig = {
    position: "top-right",
    autoClose: 3000,
    hideProgressBar: false,
    closeOnClick: false,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "light",
    transition: Slide,
  };

  showToast();
  return null;
};

ToastNotification.propTypes = {
  type: PropTypes.oneOf(["success", "error", "info", "warning"]),
  message: PropTypes.string.isRequired,
};

export default ToastNotification;
