"use client";

import React, {useState} from 'react';
import { Tenant as TenantModel } from '../../models/Tenant';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import CreatableSelect from 'react-select/creatable';
import {useRouter} from "next/navigation";
import {toast} from "react-toastify";

interface EditTenantPopupProps {
    tenant: TenantModel;
}

const EditTenantPopup: React.FC<EditTenantPopupProps> = ({ tenant }) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [formData, setFormData] = useState(tenant);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({ ...prevState, [name]: value }));
    };

    const handleUsersChange = (selectedOptions: any) => {
        const users = selectedOptions ? selectedOptions.map((option: any) => option.value) : [];
        if (users.length === 0 ) {
            toast.error("Error! At least one user is required");
            return;
        }
        setFormData(prevState => ({ ...prevState, users }));
    };

    const handleUpdate = async () => {
        if (!sanitizeDescription(formData.description)) return
        if (formData.title === '' || formData.description === '') {
            toast.error("Error! All fields with * are required");
            return
        }
        if (formData.title.length < 3) {
            toast.error("Error! Title is too short");
            return
        }
        if(formData.description.length < 10){
            toast.error("Error! Description is too short. At least 10 characters required.");
            return
        }
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
                body: JSON.stringify(formData),
            });

            if (!res.ok) {
                throw new Error(`Error saving tenant: ${res.statusText}`);
            }

            const updatedTenant = await res.json();
            console.log('Tenant updated:', updatedTenant);


            setShowPopup(false);
            toast.success("Tenant saved successfully!");
            router.refresh();
        } catch (error) {
            toast.error("Failed to save tenant.");
            console.error('Failed to save tenant:', error);
        }
    };

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
            <button onClick={() => setShowPopup(true)} type="button"
                    className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                Edit Tenant <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faEdit} />
            </button>

            {showPopup && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-3xl w-full my-10 overflow-auto">
                        <h2 className="font-semibold text-2xl">Edit Tenant</h2>
                        <p className={"font-light text-sm mb-2"}>Edit existing attributes associated with this group of Contacts. Attributes marked with * are required.</p>
                        <form>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="title">
                                    Title*
                                </label>
                                <input
                                    type="text"
                                    id="title"
                                    name="title"
                                    value={formData.title}
                                    onChange={handleChange}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                                <p className={"font-light text-xs mt-1"}>Min 3 letters</p>
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="description">
                                    Description*
                                </label>
                                <textarea
                                    id="description"
                                    name="description"
                                    value={formData.description}
                                    onChange={handleChange}
                                    className="shadow appearance-none border min-h-24 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                                <p className={"font-light text-xs mt-1"}>Max 50 words</p>
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="colorCode">
                                    Colour
                                </label>
                                <select
                                    id="colorCode"
                                    name="colorCode"
                                    value={formData.colorCode}
                                    onChange={handleChange}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                >
                                    {availableColors.map((color, index) => (
                                        <option key={index} value={color}>{color}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="users">
                                    Users
                                </label>
                                <CreatableSelect
                                    id="users"
                                    name="users"
                                    isMulti
                                    value={formData.users.map(user => ({label: user, value: user}))}
                                    onChange={handleUsersChange}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            <div className="mt-4 flex justify-center items-center">
                                <button onClick={() => setShowPopup(false)}
                                        className="btn mt-4 mx-1 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                    Close Popup
                                </button>
                                <button type="button" onClick={handleUpdate}
                                        className="btn mt-4 mx-1 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Save Changes
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditTenantPopup;
