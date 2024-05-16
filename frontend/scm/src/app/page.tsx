"use client";

import { useEffect, useState } from "react";
import { useRouter } from 'next/navigation';
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "@/firebase";

const Home = () => {
    const [user, setUser] = useState<any>(null);
    const router = useRouter();

    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
            if (!currentUser || !currentUser.emailVerified) {
                router.push('/login');
            } else {
                setUser(currentUser);
            }
        });
        return () => unsubscribe();
    }, [router]);

    if (!user) {
        return <div>Loading...</div>
    }

    return (
        <div>
            <h1>Welcome, {user.email}</h1>
            {/* Ostale strani in komponente */}
        </div>
    );
};

export default Home;
