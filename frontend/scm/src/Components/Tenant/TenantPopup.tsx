"use client";
import {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Tenant as TenantModel, Tenant} from "@/models/Tenant";
import {IconDefinition} from "@fortawesome/fontawesome-svg-core";

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
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState("Error saving tenant");
    const [alertType, setAlertType] = useState("Error saving tenant");

    const handleInputChange = (index: number, newValue: string) => {
        setInputValues(values => {
            const newValues = [...values];
            newValues[index] = newValue;
            return newValues;
        });
    };

    const handleSave = () => {
        if (!sanitizeDescription(inputValues[1])) return
        if (inputValues[0] === '' || inputValues[1] === '' || inputValues[2] === '' || inputValues[3] === '') {
            setAlertMessage("Error! All fields are required");
            setAlertType("alert-error");
            setShowAlert(true);
            return
        }
        const newTenant: Tenant = {
            id: props.tenant?.id || '',
            title: inputValues[0],
            tenantUniqueName: props.tenant?.tenantUniqueName || '',
            description: inputValues[1],
            colorCode: inputValues[2],
            users: sanitizeUsers(inputValues[3]),
            contactTags: props.tenant?.contactTags || {},
            labels: props.tenant?.labels || {},
            displayProps: props.tenant?.displayProps || []
        };
        saveTenant(newTenant, props.IdToken).then(() => {
            setAlertMessage("Tenant saved successfully");
            setAlertType("alert-success");
            setShowAlert(true);
            setTimeout(() => {
                props.onTenantAdd();
                setShowPopup(false);
                setShowAlert(false);
                setInputValues(Array(props.labels.length).fill(''));
            }, 2000);
        });
    }

    const saveTenant = async (tenant: TenantModel, IdToken: string): Promise<void> => {
        try {
            const res = await fetch(`http://localhost:8080/tenants`, {
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

    const sanitizeUsers = (users: string) => {
        return users.split(',').map(user => user.trim());
    }

    const sanitizeDescription = (description: string) => {
        if (description.length > 300) {
            setAlertMessage("Error! Description is too long");
            setAlertType("alert-error");
            setShowAlert(true);
            return false;
        }
        return true;
    }

    const availableColors: string[] = ["Red", "Green", "Blue", "Yellow", "Orange", "Purple", "Pink", "Brown", "Black", "Violet"];

    return (
        <div>
            <button onClick={() => setShowPopup(true)}
                className="btn px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                {props.buttonAction} <FontAwesomeIcon className={"text-secondary-light font-semibold"} icon={props.icon}/>
            </button>

            {showPopup && (
                <div className="absolute z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-60 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg">
                        <h2 className={"font-semibold mb-4 text-2xl"}>{props.title}</h2>
                        {props.labels.map((label, index) => (
                            <div key={index} className={"p-2 justify-between flex flex-col items-start"}>
                                <label className={"font-normal mb-1"}>{label}</label>
                                {label === 'Colour' ? (
                                    <select
                                        className={"input input-bordered w-60"}
                                        value={inputValues![index]}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                    >
                                        {availableColors.map((color, colorIndex) => (
                                            <option key={colorIndex} value={color}>{color}</option>
                                        ))}
                                    </select>
                                ) : label === 'Description' ? (
                                    <textarea
                                        className={"input input-bordered w-60 h-20"}
                                        value={inputValues![index]}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                    />
                                ) : (
                                    <input
                                        className={"input input-bordered w-60"}
                                        type="text"
                                        value={inputValues![index]}
                                        onChange={(e) => handleInputChange(index, e.target.value)}
                                    />
                                )}
                                {label === 'Users' && <p className={"font-light text-xs mt-1"}>Separate Users with a comma</p>}
                                {label === 'Description' && <p className={"font-light text-xs mt-1"}>Max 50 words</p>}
                            </div>
                        ))}
                        <div className={"mt-4 justify-center items-center flex"}>
                            <button onClick={() => setShowPopup(false)}
                                    className="btn mt-4 mx-3 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition"
                            >Close Popup
                            </button>
                            <button onClick={() => handleSave()}
                                className="btn mt-4 mx-3 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                Save
                            </button>
                        </div>
                    </div>
                    {showAlert && (
                        <div role="alert" className={`${alertType} alert fixed bottom-0 w-full`}>
                            <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6"
                                 fill="none" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                      d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                            <span>{alertMessage}.</span>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default TenantPopup;