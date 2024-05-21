import { Tenant as TenantModel } from '../../models/Tenant';
import React, {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGear} from "@fortawesome/free-solid-svg-icons";
import {toast} from "react-toastify";
import {useRouter} from "next/navigation";
import CreatableSelect from 'react-select/creatable';

interface TenantSettingsPopupProps {
    tenant: TenantModel;
}


const TenantSettingsPopup: React.FC<TenantSettingsPopupProps> = ({ tenant}) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [formData, setFormData] = useState(tenant);

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
            console.log(formData);
            setShowPopup(false);
            toast.success("Tenant settings saved successfully!");
            router.refresh();
        } catch (error) {
            toast.error("Failed to save tenant settings.");
            console.error('Failed to save tenant settings:', error);
        }
    };

    const handleDisplayPropsChange = (selectedOptions: { label: string, value: string }[]) => {
        if (selectedOptions.length > 4) {
            toast.error('You can select a maximum of 4 props.');
        } else {
            const selectedProps = selectedOptions.map(option => option.value);
            setFormData(prevState => ({ ...prevState, displayProps: selectedProps }));
        }
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
                        <h2 className="font-semibold mb-4 text-2xl">Display options</h2>
                        <p className="text-gray-700 text-sm font-bold mb-4">Choose 4 props to display:</p>
                        <form>
                            <CreatableSelect
                                id="displayProps"
                                name="displayProps"
                                isMulti
                                isCreatable={false}
                                value={formData.displayProps.map(prop => ({ label: prop, value: prop }))}
                                options={formData.labels && Object.values(formData.labels).map((value) => ({ label: value, value: value }))}
                                onChange={handleDisplayPropsChange}
                                className="shadow appearance-none border mb-10 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            />
                            <div className="max-h-80 overflow-auto grid grid-cols-3 gap-2">
                                {formData.labels && Object.entries(formData.labels).map(([key, value], index) => (
                                    <div key={index} className="mb-4 flex justify-center items-center">
                                        <label className="block text-gray-700 text-sm font-bold mr-5"
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
                                        className="btn mt-4 mx-1 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                    Close Popup
                                </button>
                                <button type="button" onClick={handleSave}
                                        className="btn mt-4 mx-1 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
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