// AuthCheck.tsx
"use client";

import { useEffect, ReactNode } from 'react';
import { useRouter } from 'next/navigation';
import { auth } from "@/firebase";
import { onAuthStateChanged, getIdTokenResult, User } from 'firebase/auth';
import Cookies from 'js-cookie'; // Dodamo js-cookie za upravljanje cookijev

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
                    console.log("No token found in cookies.");
                    return false;
                }

                if (tokenResult.token !== token) {
                    return false;
                }

                const currentTime = new Date().getTime() / 1000;
                const expirationTime = tokenResult.expirationTime ? new Date(tokenResult.expirationTime).getTime() / 1000 : 0;

                if (expirationTime < currentTime) {
                    console.log("Token has expired.");
                    return false;
                }

                return true;
            } catch (error) {
                console.error('Error validating IdToken:', error);
                return false;
            }
        };

        const unsubscribe = onAuthStateChanged(auth, async (user) => {
            const token = Cookies.get("IdToken"); // Preveri token iz cookija
            if (!token) {
                console.log("No token found in cookies, redirecting to login.");
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
