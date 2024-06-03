"use client";

import { useEffect, useState } from "react";
import { useRouter } from 'next/navigation';
import { onAuthStateChanged } from "firebase/auth";
import { auth } from "@/firebase";
import TenantsDashboard from "@/Components/Tenant/TenantsDashboard";
import 'react-toastify/dist/ReactToastify.css';

const Home = () => {
    const [IdToken, setIdToken] = useState<string>();
    const router = useRouter();


    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, async (currentUser) => {
            if (!currentUser) {
                router.push('/login');
            } else {
                const idToken = await currentUser.getIdToken();
                if(idToken === "" || idToken === null){
                    router.push('/login');
                }
                if (idToken !== undefined) {
                    setIdToken(idToken);
                }
            }
        });
        return () => unsubscribe();
    }, [router]);

    return (
        <>
        <head>
            <title>SCM - Home</title>
        </head>
        <div className="container mx-auto p-4">
            {IdToken !== undefined && (
                <TenantsDashboard IdToken={IdToken}/>
            )}
        </div>
        </>
    );
};

export default Home;
