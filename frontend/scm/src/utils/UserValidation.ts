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
    const minLength = 8;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (password.length < minLength) {
        toast.error("Password must be at least 8 characters long.");
        return false;
    }
    if (!hasUpperCase) {
        toast.error("Password must contain at least one uppercase letter.");
        return false;
    }
    if (!hasLowerCase) {
        toast.error("Password must contain at least one lowercase letter.");
        return false;
    }
    if (!hasNumber) {
        toast.error("Password must contain at least one number.");
        return false;
    }
    if (!hasSpecialChar) {
        toast.error("Password must contain at least one special character.");
        return false;
    }
    return true;
}


export const checkConfirmPassword = (password: string, confirmPassword: string) => {
    if (password !== confirmPassword) {
        toast.error("Passwords do not match!");
        return false;
    }
    return true;
}