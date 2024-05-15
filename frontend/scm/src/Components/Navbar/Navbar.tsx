"use client";
import styles from './Navbar.module.css';
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser} from "@fortawesome/free-regular-svg-icons";
import {useEffect, useState} from "react";
import {usePathname} from 'next/navigation';
import {onAuthStateChanged, signOut} from "firebase/auth";
import {auth} from "@/firebase";


const Navbar = () => {
    const [user, setUser] = useState<any>(null);
    const pathname = usePathname();

    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
            setUser(currentUser);
        });
        return () => unsubscribe();
    }, []);

    const handleLogout = async () => {
        try {
            await signOut(auth);
            // Po odjavi lahko preusmerite uporabnika nazaj na prijavno stran
            window.location.href = '/login';
        } catch (error) {
            console.error("Error logging out:", error);
        }
    };

    if (!user || pathname === '/login') {
        return null;
    }

    return (
        <div className={"bg-primary-light flex justify-between text-white"}>
            <div className={"flex"}>
                <img className={styles.logo} src={"/logo-scm.png"} alt={"Logo"}/>
                <div className={"items-center justify-center flex"}>
                    <nav className={"navbar"}>
                        <ul>
                            <li className={"m-2 p-3 rounded-8 font-semibold hover:bg-primary-dark"}>
                                <Link href={"/tenants"}>Tenants</Link>
                            </li>
                            <li className={"m-2 p-3 rounded-8 font-semibold hover:bg-primary-dark"}>
                                <Link href={"/saved-searches"}>Saved Searches</Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            <div className={"items-center justify-center flex mr-10"}>
                {user && (
                    <div>
                        <div className={styles.userEmail}>{user.email}</div>
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Navbar;