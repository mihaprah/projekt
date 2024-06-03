"use client"
import {createUserWithEmailAndPassword, sendEmailVerification, signInWithEmailAndPassword} from "firebase/auth";
import {auth} from "@/firebase";
import {useRouter} from "next/navigation";
import React, {useState} from "react";
import Link from "next/link";
import {toast, ToastContainer} from "react-toastify";
import {checkConfirmPassword, checkEmail, checkIfEmpty, checkPassword} from "@/utils/UserValidation";
import 'react-toastify/dist/ReactToastify.css';
import Loading from "@/app/loading";
import Image from "next/image";

const Register = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const router = useRouter();

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        if (!checkIfEmpty(email, password)) return setLoading(false);
        if (!checkEmail(email)) return setLoading(false);
        if (!checkPassword(password)) return setLoading(false);
        if(!checkConfirmPassword(password, confirmPassword)) return setLoading(false);

        try {
            await signInWithEmailAndPassword(auth, email, password).then(() => {
                setLoading(false);
                toast.error("User already exists.");
            });
        } catch (error: any) {
            if (error.code === 'auth/invalid-credential') {
                try {
                    const userCredential = await createUserWithEmailAndPassword(auth, email, password);
                    const user = userCredential.user;
                    if (user) {
                        await sendEmailVerification(user).then(() => {
                            setEmail("");
                            setPassword("");
                            setLoading(false);
                            startCountdown();
                            toast.success(`Please verify your email before logging in. Redirecting to login page in 5 seconds.`);
                        });
                    }
                } catch (error) {
                    setLoading(false);
                    toast.error("Something went wrong. Please try again.");
                }
            } else {
                setLoading(false);
                toast.error("Something went wrong. Please try again.");
            }
        }
    };

    const startCountdown = () => {
        setTimeout(() => {
            router.push('/login');
        }, 5000);
    }

    return (
        <>
            <head>
                <title>SCM - Register</title>
            </head>
            {loading && (
                <div
                    className={"fixed top-0 left-0 w-full h-full bg-white bg-opacity-50 z-50 flex items-center justify-center"}>
                    <Loading/>
                </div>
            )}
            <ToastContainer/>
            <div className={"flex flex-col items-center justify-center min-h-screen"}>
                <div className={"flex flex-col items-center justify-center rounded-8 p-10"}>
                    <Image width={700} height={700} className={"w-48 h-auto rounded-8"} src={"/logo-scm.png"}
                           alt={"Logo"}/>
                    <h3 className={"text-xl font-semibold mb-5 dark:text-white dark:pt-6"}>Register for new Account</h3>
                    <form onSubmit={handleRegister} className={"flex flex-col items-center justify-center"}>
                        <label
                            className="input input-bordered flex items-center gap-2 mb-2 text-secondary-dark dark:bg-white">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor"
                                 className="w-4 h-4 opacity-70">
                                <path
                                    d="M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z"/>
                                <path
                                    d="M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z"/>
                            </svg>
                            <input
                                type="text"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="Email"
                            />
                        </label>
                        <label
                            className="input input-bordered flex items-center gap-2 mb-2 text-secondary-dark dark:bg-white">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor"
                                 className="w-4 h-4 opacity-70">
                                <path fillRule="evenodd"
                                      d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
                                      clipRule="evenodd"/>
                            </svg>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password"
                            />
                        </label>
                        <label
                            className="input input-bordered flex items-center gap-2 mb-2 text-secondary-dark dark:bg-white">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor"
                                 className="w-4 h-4 opacity-70">
                                <path fillRule="evenodd"
                                      d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
                                      clipRule="evenodd"/>
                            </svg>
                            <input
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                placeholder="Confirm Password"
                            />
                        </label>
                        <p className={"text-xs text-gray-400 break-words"}>Password must be at least 6 characters
                            long.</p>
                        {password.length > 5 && password === confirmPassword ? (
                            <p className={"text-xs text-success"}>Passwords match!</p>
                        ) : (
                            password !== confirmPassword &&
                            <p className={"text-xs text-error"}>Passwords do not match!</p>
                        )}
                        <button type="submit"
                                className={"btn px-4 mt-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark"}>Register
                        </button>
                    </form>
                    <Link href={"/login"} className={"mt-5 link text-primary-light dark:text-white text-xs"}>Already
                        have an
                        account?
                        Log in here!</Link>
                </div>
            </div>
        </>
    );
};

export default Register;