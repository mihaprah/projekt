"use client";
import React, {useEffect, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Tenant as TenantModel, Tenant} from "@/models/Tenant";
import {IconDefinition} from "@fortawesome/fontawesome-svg-core";
import {onAuthStateChanged} from "firebase/auth";
import {auth} from "@/firebase";
import {useRouter} from 'next/navigation';
import CreatableSelect from "react-select/creatable";
import {toast, ToastContainer} from "react-toastify";

interface TenantAddPopupProps {
    buttonAction: string;
    icon: IconDefinition;
    title: string;
    labels: string[];
    initialValues?: string[];
    IdToken: string;
    tenant?: Tenant;
    onTenantAdd: () => void;
}

const TenantPopup: React.FC<TenantAddPopupProps> = (props) => {
    const [showPopup, setShowPopup] = useState(false);
    const [inputValues, setInputValues] = useState(
        props.initialValues || Array(props.labels.length).fill('')
    );

    const [user, setUser] = useState<any>(null);
    const router = useRouter();
    const [users, setUsers] = useState<string[]>([]);

    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, async (currentUser) => {
            if (!currentUser) {
                router.push('/login');
            } else {
                setUser(currentUser);
            }
        });
        return () => unsubscribe();
    }, [router]);

    const handleInputChange = (index: number, newValue: string) => {
        setInputValues(values => {
            const newValues = [...values];
            newValues[index] = newValue;
            return newValues;
        });
    };

    const handleSave = () => {
        if (!sanitizeDescription(inputValues[1])) return
        if (inputValues[0] === '' || inputValues[1] === '') {
            toast.error("Error! All fields are required");
            return
        }
        if (inputValues[0].length < 3) {
            toast.error("Error! Title is too short");
            return
        }
        users.push(user.email)
        const newTenant: Tenant = {
            id: props.tenant?.id || '',
            title: inputValues[0],
            tenantUniqueName: props.tenant?.tenantUniqueName || '',
            description: inputValues[1],
            colorCode: inputValues[2] || 'Red',
            users: users,
            contactTags: props.tenant?.contactTags || {},
            labels: props.tenant?.labels || {},
            displayProps: props.tenant?.displayProps || []
        };
        saveTenant(newTenant, props.IdToken).then(() => {
            toast.success("Tenant saved successfully!");
            props.onTenantAdd();
            setShowPopup(false);
            setInputValues(Array(props.labels.length).fill(''));
            setUsers([]);
        });
    }

    const saveTenant = async (tenant: TenantModel, IdToken: string): Promise<void> => {
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                },
                body: JSON.stringify(tenant),
            });

            if (!res.ok) {
                throw new Error(`Error saving tenant: ${res.statusText}`);
            }
        } catch (error) {
            console.error('Failed to save tenant:', error);
        }
    }

    const handleUsersChange = (newValue: any) => {
        const users = newValue ? newValue.map((option: any) => option.value) : [];
        setUsers(users);
    }

    const sanitizeDescription = (description: string) => {
        if (description.length > 300) {
            toast.error("Error! Description is too long");
            return false;
        }
        return true;
    }

    const availableColors: string[] = ["Red", "Green", "Blue", "Yellow", "Orange", "Purple", "Pink", "Brown", "Black", "Violet"];

    return (
        <div>
            <ToastContainer />
            <button onClick={() => setShowPopup(true)}
                className="px-4 py-1 bg-primary-light text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                {props.buttonAction} <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={props.icon}/>
            </button>

            {showPopup && (
                <div className="absolute z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-60 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg w-600px">
                        <h2 className={"font-semibold mb-4 text-2xl"}>{props.title}</h2>
                        {props.labels.map((label, index) => (
                            <div key={index} className={"p-2 justify-between flex flex-col items-start"}>
                                <label className={"font-normal mb-1"}>{label}</label>
                                {label === 'Colour' ? (
                                    <select
                                        className={"input input-bordered w-full"}
                                        value={inputValues![index]}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                    >
                                        {availableColors.map((color, colorIndex) => (
                                            <option key={colorIndex} value={color}>{color}</option>
                                        ))}
                                    </select>
                                ) : label === 'Description' ? (
                                    <textarea
                                        className={"input input-bordered w-full h-20"}
                                        value={inputValues![index]}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                    />
                                ) : label === 'Other users' ? (
                                        <CreatableSelect
                                            id="tags"
                                            name="tags"
                                            isMulti
                                            className="rounded-8 w-full"
                                            onChange={handleUsersChange}
                                        />
                                    )
                                : (
                                    <input
                                        className={"input input-bordered w-full"}
                                        type="text"
                                        value={inputValues![index]}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                    />
                                )}
                                {label === 'Description' && <p className={"font-light text-xs mt-1"}>Max 50 words</p>}
                                {label === 'Title' && <p className={"font-light text-xs mt-1"}>Min 3 letters</p>}
                                {label === 'Other users' && <p className={"font-light text-xs mt-1"}>You are already added</p>}
                            </div>
                        ))}
                        <div className={"mt-4 justify-center items-center flex"}>
                            <button onClick={() => setShowPopup(false)}
                                    className="btn mt-4 mx-1 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition"
                            >Close Popup
                            </button>
                            <button onClick={() => handleSave()}
                                className="btn mt-4 mx-1 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                Add Tenant
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TenantPopup;