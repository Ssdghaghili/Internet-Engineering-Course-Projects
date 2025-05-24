import { toast, Slide } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export const ToastNotification = ({ type = "success", message = "Notification!" }) => {
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

export default ToastNotification;