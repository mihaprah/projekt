"use client";
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faUsersRectangle, faCircleUser} from "@fortawesome/free-solid-svg-icons";
import {useEffect, useState} from "react";
import {usePathname} from 'next/navigation';
import {onAuthStateChanged, signOut} from "firebase/auth";
import {auth} from "@/firebase";
import Cookies from "js-cookie";
import Image from "next/image";
import {toast} from "react-toastify";


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
            Cookies.remove('IdToken');
            window.location.href = '/login';
        } catch (error: any) {
            toast.error(error.message || "Failed to logout")
        }
    };

    if (!user || pathname === '/login' || pathname === '/register' || !user.emailVerified) {
        return null;
    }

    return (
        <div className={"bg-primary-light flex justify-between text-white dark:bg-primary-dark"}>
            <div className={"flex"}>
                <Link href={"/"}>
                        <Image width={700} height={700} className={"w-150px"} src={"/logo-scm.png"} alt={"Logo"}/>
                </Link>
                <div className={"items-center justify-center flex"}>
                    <nav className={"navbar"}>
                        <ul>
                            <li className={`m-2 p-3 rounded-8 hover:bg-primary-dark hover:scale-105 transition hover:shadow dark:hover:bg-primary-light ${pathname === '/' ? 'scale-105 transition bg-primary-dark' : ''}`}>
                                <Link href={"/"}>
                                    <span className={"mr-2 font-semibold"}>Home</span>
                                    <FontAwesomeIcon className={"w-5 h-auto ml-1 "} icon={faUsersRectangle}/>
                                </Link>
                            </li>
                            <li className={`m-2 p-3 rounded-8 hover:bg-primary-dark hover:scale-105 transition hover:shadow dark:hover:bg-primary-light ${pathname === '/saved-searches' ? 'scale-105 transition bg-primary-dark' : ''}`}>
                            <Link href={"/saved-searches"}>
                                    <span className={"mr-2 font-semibold"}>Saved Searches</span>
                                    <FontAwesomeIcon className={"w-5 h-auto ml-1"} icon={faMagnifyingGlass} />
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
                            <FontAwesomeIcon className={`ml-1 w-5 h-auto p-2`} icon={faCircleUser} />
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