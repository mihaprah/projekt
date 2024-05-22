"use client";
import { Tenant as TenantModel } from '../../models/Tenant';
import React, {useEffect, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGear} from "@fortawesome/free-solid-svg-icons";
import {toast} from "react-toastify";
import {useRouter} from "next/navigation";
import Select from "react-select";

interface TenantSettingsPopupProps {
    tenant: TenantModel;
    IdToken: string;
}


const updateLabels = async (labels: any, tenantId: string, IdToken: string) => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/labels/${tenantId}`, {
            method: 'PUT',
            headers: {
                'userToken': `Bearer ${IdToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(labels)
        });

        if (!res.ok) {
            throw new Error(`Error updating labels: ${res.statusText}`);
        }

    } catch (error) {
        toast.error("Failed to save tenant settings.");
        return [];
    }
}

const updateDisplayProps = async (displayProps: string[], tenantId: string, IdToken: string) => {
    console.log(displayProps);
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/displayProps/${tenantId}`, {
            method: 'PUT',
            headers: {
                'userToken': `Bearer ${IdToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(displayProps)
        });

        if (!res.ok) {
            throw new Error(`Error updating display props: ${res.statusText}`);
        }

    } catch (error) {
        toast.error("Failed to save display properties.");
        return [];
    }
}


const TenantSettingsPopup: React.FC<TenantSettingsPopupProps> = ({ tenant, IdToken }) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [formData, setFormData] = useState({
        ...tenant,
        displayProps: tenant.displayProps.length ? tenant.displayProps : ['name', 'phoneNumber', 'email']
    });

    useEffect(() => {
        setFormData(tenant);
    }, [tenant]);

    const handleLabelChange = (key: string) => (e: React.ChangeEvent<HTMLInputElement>) => {
        const newValue = e.target.value;
        setFormData(prevState => ({
            ...prevState,
            labels: {
                ...prevState.labels,
                [key]: newValue
            }
        }));
    };

    const handleSave = async () => {
        try {
            await updateLabels(formData.labels, tenant.id, IdToken);
            await updateDisplayProps(formData.displayProps, tenant.id, IdToken);
            setShowPopup(false);
            toast.success("Tenant settings saved successfully!");
            router.refresh();
        } catch (error) {
            console.log(error);
            toast.error("Failed to save tenant settings.");
        }
    };

    const handleDisplayPropsChange = (selectedOptions: ReadonlyArray<{ label: string, value: string }>) => {
        const selectedProps = selectedOptions.map(option => option.value);
        setFormData(prevState => ({ ...prevState, displayProps: selectedProps }));
    };


    const getFilteredPropsOptions = () => {
        return Object.entries(formData.labels)
            .filter(([key]) => !formData.displayProps.includes(key))
            .map(([key, value]) => ({ label: value, value: key }));
    };

    return (
        <div>
            <button type="button" onClick={() => setShowPopup(true)}
                    className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                Tenant Settings
                <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faGear}/>
            </button>

            {showPopup && (
                <div className="absolute z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-60 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-5xl w-full">
                        <h2 className="font-semibold mb-4 text-2xl">Tenant Settings</h2>
                        <h4 className={"text-gray-700 font-semibold"}>Display properties</h4>
                        <p className="text-gray-700 text-sm mb-4 mt-1">Choose 4 props to display</p>
                        <form>
                            <Select
                                id="displayProps"
                                name="displayProps"
                                isMulti
                                value={formData.displayProps.map(prop => ({
                                    label: formData.labels[prop],
                                    value: prop
                                }))}
                                options={getFilteredPropsOptions()}
                                onChange={handleDisplayPropsChange}
                                className="appearance-none border-0 mb-10 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            />
                            <h4 className={"text-gray-700 font-semibold mb-3"}>Label names</h4>
                            <div className="max-h-80 overflow-auto grid grid-cols-3 gap-2">
                                {formData.labels && Object.entries(formData.labels).map(([key, value], index) => (
                                    <div key={index} className="mb-4 flex justify-end items-center">
                                        <label className="block text-gray-700 text-sm font-semibold mr-5"
                                               htmlFor="colorCode">
                                            {key}
                                        </label>
                                        <input
                                            type="text"
                                            name={key}
                                            value={value}
                                            className="border border-gray-300 rounded-8 w-40 p-2"
                                            onChange={handleLabelChange(key)}
                                        />
                                    </div>
                                ))}
                            </div>
                            <div className="mt-4 flex justify-center items-center">
                                <button onClick={() => setShowPopup(false)}
                                        className="mt-4 mx-1 px-4 py-1 bg-danger text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                    Close Popup
                                </button>
                                <button type="button" onClick={handleSave}
                                        className="mt-4 mx-1 px-4 py-1 bg-primary-light text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Save Settings
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default TenantSettingsPopup;
