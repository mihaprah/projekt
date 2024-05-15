"use client";
import styles from './Navbar.module.css';
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faUsersRectangle, faCircleUser} from "@fortawesome/free-solid-svg-icons";
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
            // TODO Po odjavi lahko preusmerite uporabnika nazaj na prijavno stran
            window.location.href = '/login';
        } catch (error) {
            console.error("Error logging out:", error);
        }
    };

    if (!user || pathname === '/login') {
        return null;
    }

    return (
        <div className={"bg-primary-light flex justify-between text-white dark:bg-primary-dark"}>
            <div className={"flex"}>
                <img className={styles.logo} src={"/logo-scm.png"} alt={"Logo"}/>
                <div className={"items-center justify-center flex"}>
                    <nav className={"navbar"}>
                        <ul>
                            <li className={"m-2 p-3 rounded-8 hover:bg-primary-dark hover:scale-105 transition hover:shadow dark:hover:bg-primary-light "}>
                                <Link href={"/tenants"}>
                                    <span className={"mr-2 font-semibold"}>Tenants</span>
                                    <FontAwesomeIcon className={"h-5 w-auto ml-1"} icon={faUsersRectangle}/>
                                </Link>
                            </li>
                            <li className={"m-2 p-3 rounded-8 hover:bg-primary-dark hover:scale-105 transition hover:shadow dark:hover:bg-primary-light"}>
                            <Link href={"/saved-searches"}>
                                    <span className={"mr-2 font-semibold"}>Saved Searches</span>
                                    <FontAwesomeIcon className={"h-5 w-auto ml-1"} icon={faMagnifyingGlass} />
                                </Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            <div className={"items-center justify-center flex mr-10"}>
                {user && (
                    <div className="dropdown dropdown-end">
                        <div tabIndex={0} role="button"
                             className="btn bg-primary-light dark:bg-primary-dark text-white border-none m-1 shadow-none hover:bg-primary-dark hover:scale-105 transition hover:shadow dark:hover:bg-primary-light">
                            <span className={"p-2 font-medium"}>{user.email}</span>
                            <FontAwesomeIcon className={`${styles.icon} p-2`} icon={faCircleUser} />
                        </div>
                        <ul tabIndex={0}
                            className="dropdown-content z-[1] menu p-2 shadow-none rounded-8 w-52 bg-primary-light dark:bg-primary-dark">
                            <li className={"px-3 py-2 mx-2 rounded-8 hover:bg-primary-dark dark:hover:bg-primary-light hover:scale-105 transition hover:cursor-pointer"} onClick={handleLogout}>
                                <span>Logout</span>
                            </li>
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Navbar;