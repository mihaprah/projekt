"use client";

import { useEffect, useState } from "react";
import { useRouter } from 'next/navigation';
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "@/firebase";
import TenantsDashboard from "@/Components/Tenant/TenantsDashboard";
import 'react-toastify/dist/ReactToastify.css';

const Home = () => {
    const [user, setUser] = useState<any>(null);
    const [token, setToken] = useState<string | null>("");
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
        <div className="container mx-auto p-4">
            <TenantsDashboard IdToken={token || ""}/>
        </div>
    );
};

export default Home;
