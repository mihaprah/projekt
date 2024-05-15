import React from 'react';
import styles from './Navbar.module.css';
import Link from "next/link";

const Navbar = () => {
    return (
        <div className={"bg-primary-light flex justify-between text-white"}>
            <div className={"flex"}>
                <img className={styles.logo} src={"/logo-scm.png"} alt={"Logo"}/>
                <div className={"items-center justify-center flex"}>
                    <nav className={"navbar"}>
                        <ul>
                            <li className={"m-2 p-2 rounded-10 hover:bg-primary-dark"}>
                                <Link href={"/tenants"}>Tenants</Link>
                            </li>
                            <li className={"m-2 p-2 rounded-10 hover:bg-primary-dark"}>
                                <Link href={"/saved-searches"}>Saved Searches</Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            <div className={"items-center justify-center flex mr-4"}>
                username
            </div>
        </div>
    );
};

export default Navbar;