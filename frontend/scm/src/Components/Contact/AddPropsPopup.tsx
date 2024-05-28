import React, { useState } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {useRouter} from "next/navigation";
import Select from "react-select";

interface AddPropsPopupProps {
    tenantUniqueName: string;
    selectedContacts: string[];
    IdToken: string;
    tenantId: string;
    availableProps: { label: string, value: string }[];
    onClose: () => void;
    onSave: () => void;
}

const AddPropsPopup: React.FC<AddPropsPopupProps> = ({
                                                         tenantUniqueName,
                                                         selectedContacts,
                                                         IdToken,
                                                         tenantId,
                                                         availableProps,
                                                         onClose,
                                                         onSave
                                                     }) => {
    const [selectedProp, setSelectedProp] = useState<{ label: string, value: string } | null>(null);
    const [propValue, setPropValue] = useState<string>("");
    const router = useRouter();

    const handlePropChange = (newValue: any) => {
        setSelectedProp(newValue);
    };

    const handleSaveProps = async () => {
        if (!selectedProp || !propValue) {
            toast.error('Please select a property and enter a value');
            return;
        }

        const propData = {
            [selectedProp.value]: propValue
        };

        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/props/multiple/add/${tenantUniqueName}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                    'tenantId': tenantId
                },
                body: JSON.stringify({ contactIds: selectedContacts, propData })
            });

            if (!res.ok) {
                toast.error(res.statusText || 'Failed to add property');
            }

            toast.success('Property added successfully');
            onSave();
            router.refresh();
        } catch (error: any) {
            toast.error(error.message || 'Failed to add property');
        }
    };

    return (
        <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
            <div className="bg-white p-8 rounded-lg shadow-lg max-w-3xl w-full">
                <div className="flex justify-between items-center">
                    <h2 className="text-xl font-semibold">Add existing property</h2>
                </div>
                <p className={"font-light text-sm mb-4"}>Property will be updated or added to all selected contacts.</p>
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="prop">
                        Property
                    </label>
                    <Select
                        id="prop"
                        name="prop"
                        options={availableProps}
                        onChange={handlePropChange}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="value">
                        Value
                    </label>
                    <input
                        type="text"
                        id="value"
                        name="value"
                        value={propValue}
                        onChange={(e) => setPropValue(e.target.value)}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    />
                </div>
                <div className="flex justify-end">
                    <button
                        onClick={onClose}
                        className="btn px-4 btn-sm bg-red-600 border-0 text-white rounded-8 font-semibold hover:scale-105 transition hover:bg-red-700 mr-2">
                        Close Popup
                    </button>
                    <button
                        onClick={handleSaveProps}
                        className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Add Property
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AddPropsPopup;
