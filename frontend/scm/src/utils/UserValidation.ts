import {toast} from "react-toastify";

export const checkIfEmpty = (email: string, pass: string) => {
    if (email === "" || pass === "") {
        toast.error("Please fill in all fields.");
        return false;
    }
    return true;
}

export const checkEmail = (email: string) => {
    const re = /\S+@\S+\.\S+/;
    if (!re.test(email)) {
        toast.error("Invalid email address.");
        return false;
    }
    return true
}

export const checkPassword = (password: string) => {
    if (password.length < 6) {
        toast.error("Password must be at least 6 characters long.");
        return false;
    }
    return true;
}