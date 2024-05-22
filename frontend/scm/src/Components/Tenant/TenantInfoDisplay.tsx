"use client";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faUsers, faTrash, faArrowLeft} from "@fortawesome/free-solid-svg-icons";
import { ToastContainer, toast } from "react-toastify";
import EditTenantPopup from "@/Components/Tenant/EditTenantPopup";
import AddNewContactPopup from "@/Components/Contact/AddNewContactPopup";
import TenantSettingsPopup from "@/Components/Tenant/TenantSettingsPopup";
import React, { useState } from "react";
import {Tenant as TenantModel} from "@/models/Tenant";
import { useRouter } from "next/navigation";

interface TenantInfoDisplayProps {
    tenant: TenantModel;
    contactsNumber: number;
    IdToken: string;
    onSave: () => void;
    numberOfTenants: number;
}

const TenantInfoDisplay: React.FC<TenantInfoDisplayProps> = (props) => {
    const router = useRouter();
    const [showConfirmation, setShowConfirmation] = useState(false);


    const handleDeleteTenant = async () => {
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/deactivate/${props.tenant.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
            });

            if (!res.ok) {
                throw new Error(`Error deactivating tenant: ${res.statusText}`);
            }

            toast.success("Tenant deactivated successfully!");
            router.push('/'); // Redirect to home or any other page after deletion
        } catch (error) {
            toast.error("Failed to deactivate tenant.");
            console.error('Failed to deactivate tenant:', error);
        }
    };

    return (
        <div>
            <ToastContainer />
            <div className="flex justify-between mb-4">
                <div>
                    <div className={"flex items-center"}>
                        {props.numberOfTenants> 1 && (
                            <div className="flex items-center mb-4">
                                <FontAwesomeIcon
                                    icon={faArrowLeft}
                                    className="text-primary-light mr-4 mt-4 cursor-pointer w-3.5 h-auto"
                                    onClick={() => {router.push("/"); router.refresh();}}
                                />
                            </div>
                        )}
                        <h2 className="text-3xl font-semibold text-primary-light">{props.tenant.title}</h2>
                        <span className="ml-6 mr-2 text-2xl font-semibold ">{props.contactsNumber}</span>
                        <FontAwesomeIcon className="h-5 w-auto mb-0.5 " icon={faUsers} />
                    </div>
                    <p className="text-m text-gray-600 mt-1 break-words max-w-96">{props.tenant.description}</p>
                    <div className="mt-4 flex items-center space-x-3">
                        <EditTenantPopup tenant={props.tenant} />
                        <TenantSettingsPopup tenant={props.tenant} IdToken={props.IdToken}/>
                        <button
                            onClick={() => setShowConfirmation(true)}
                            className="btn px-4 btn-sm bg-red-600 border-0 text-white rounded-8 font-semibold hover:scale-105 transition hover:bg-red-700">
                            Delete Tenant <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faTrash} />
                        </button>
                    </div>
                </div>
                <div className="flex items-end space-x-3">
                    <AddNewContactPopup tenantUniqueName={props.tenant.tenantUniqueName} onSave={props.onSave} />
                </div>
            </div>
            {showConfirmation && (
                <div className="fixed z-50 inset-0 flex items-center justify-center bg-gray-800 bg-opacity-75">
                    <div className="bg-white p-8 rounded-lg shadow-lg relative z-50">
                        <h2 className="text-xl mb-4">Are you sure you want to delete this tenant?</h2>
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowConfirmation(false)}
                                className="px-4 py-1 rounded-8 bg-gray-300 text-black font-semibold mr-2">
                                Cancel
                            </button>
                            <button
                                onClick={handleDeleteTenant}
                                className="px-4 py-1 rounded-8 bg-danger font-semibold text-white">
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TenantInfoDisplay;
