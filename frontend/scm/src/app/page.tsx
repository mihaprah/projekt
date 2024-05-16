"use client";

import { useEffect, useState } from "react";
import { useRouter } from 'next/navigation';
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "@/firebase";

const Home = () => {
    const [user, setUser] = useState<any>(null);
    const [token, setToken] = useState<string | null>(null);
    const router = useRouter();

    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, async (currentUser) => {
            if (!currentUser) {
                router.push('/login');
            } else {
                setUser(currentUser);
                const idToken = await currentUser.getIdToken();
                setToken(idToken);
            }
        });
        return () => unsubscribe();
    }, [router]);

    if (!user) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <h1>Welcome, {user.email}, token: {token}</h1>
        </div>
    );
};

export default Home;
