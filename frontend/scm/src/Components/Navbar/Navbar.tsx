import styles from './Navbar.module.css';
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-regular-svg-icons";


const Navbar = () => {

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
                <div className="dropdown dropdown-end">
                    <div tabIndex={0} role="button" className="btn bg-primary-light text-white border-none m-1 shadow-none hover:bg-primary-dark">
                        <span className={"p-2 font-normal"}>user1@example.com</span>
                        <FontAwesomeIcon className={`${styles.icon} p-2`} icon={faUser}/>
                    </div>
                    <ul tabIndex={0} className="dropdown-content z-[1] menu p-2 shadow rounded-8 w-52 bg-primary-light">
                        <li className={"hover:bg-primary-dark hover:rounded-8"}><a>Logout</a></li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default Navbar;