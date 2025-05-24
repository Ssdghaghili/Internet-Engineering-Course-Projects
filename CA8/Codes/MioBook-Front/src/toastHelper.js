import { toast, Slide } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export const showToast = (type = "success", message = "Notification!") => {
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

  switch (type) {
    case "success":
      toast.success(message, toastConfig);
      break;
    case "error":
      toast.error(message, toastConfig);
      break;
    case "info":
      toast.info(message, toastConfig);
      break;
    case "warning":
      toast.warning(message, toastConfig);
      break;
    default:
      toast(message, toastConfig);
  }
};