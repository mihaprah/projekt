// AuthCheck.tsx
"use client";

import { useEffect, ReactNode } from 'react';
import { useRouter } from 'next/navigation';
import { auth } from "@/firebase";
import { onAuthStateChanged, getIdTokenResult, User } from 'firebase/auth';
import Cookies from 'js-cookie';
import {toast} from "react-toastify"; // Dodamo js-cookie za upravljanje cookijev

interface AuthCheckProps {
    children: ReactNode;
}

const AuthCheck: React.FC<AuthCheckProps> = ({ children }) => {
    const router = useRouter();

    useEffect(() => {
        const checkTokenValidity = async (user: User | null) => {
            if (!user) {
                return false;
            }

            try {
                const tokenResult = await getIdTokenResult(user);
                const token = Cookies.get("IdToken"); // Preveri token iz cookija

                if (!token) {
                    toast.error("No token found in cookies.");
                    return false;
                }

                if (tokenResult.token !== token) {
                    return false;
                }

                const currentTime = new Date().getTime() / 1000;
                const expirationTime = tokenResult.expirationTime ? new Date(tokenResult.expirationTime).getTime() / 1000 : 0;

                if (expirationTime < currentTime) {
                    toast.error("Token has expired.");
                    return false;
                }

                return true;
            } catch (error: any) {
                toast.error(error.message);
                return false;
            }
        };

        const unsubscribe = onAuthStateChanged(auth, async (user) => {
            const token = Cookies.get("IdToken"); // Preveri token iz cookija
            if (!token) {
                router.push('/login');
                return;
            }

            const isValid = await checkTokenValidity(user);

            if (!isValid) {
                router.push('/login');
            }
        });

        return () => unsubscribe();
    }, [router]);

    return <>{children}</>;
};

export default AuthCheck;
