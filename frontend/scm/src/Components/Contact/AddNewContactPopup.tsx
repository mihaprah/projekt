"use client";
import React, { useState, useEffect } from 'react';
import { Contact as ContactModel } from '../../models/Contact';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useRouter } from "next/navigation";
import CreatableSelect from "react-select/creatable";

interface AddNewContactPopupProps {
    tenantUniqueName: string;
    onSave: () => void;
    initialContactData?: ContactModel; // Optional prop for initial contact data
    onClose?: () => void; // Optional prop for closing the popup
}

const AddNewContactPopup: React.FC<AddNewContactPopupProps> = ({ tenantUniqueName, onSave, initialContactData, onClose }) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [formData, setFormData] = useState<ContactModel>({
        id: '',
        title: '',
        user: '',
        tenantUniqueName: tenantUniqueName,
        comments: '',
        tags: [],
        props: {},
        createdAt: new Date(),
        attributesToString: ""
    });

    const [availableTags, setAvailableTags] = useState<{ label: string, value: string }[]>([]);
    const [availablePropsKeys, setAvailablePropsKeys] = useState<string[]>([]);
    const [newProps, setNewProps] = useState<{ key: string, value: string }[]>([{ key: '', value: '' }]);

    useEffect(() => {
        const fetchTags = async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/unique/${tenantUniqueName}`, {
                    headers: {
                        'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                    },
                });

                if (!res.ok) {
                    throw new Error(`Error fetching tags: ${res.statusText}`);
                }

                const tenant = await res.json();
                const tags = Object.keys(tenant.contactTags).map(tag => ({ label: tag, value: tag }));
                setAvailableTags(tags);
            } catch (error) {
                console.error('Failed to fetch tags:', error);
            }
        };

        const fetchPropsKeys = async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/unique/${tenantUniqueName}`, {
                    headers: {
                        'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                    },
                });

                if (!res.ok) {
                    throw new Error(`Error fetching contacts: ${res.statusText}`);
                }

                const tenant = await res.json();
                if (tenant) {
                    const propsKeys = Object.keys(tenant.labels);
                    setAvailablePropsKeys(propsKeys);
                }
            } catch (error) {
                console.error('Failed to fetch props keys:', error);
            }
        };

        fetchTags();
        fetchPropsKeys();
    }, [tenantUniqueName]);

    useEffect(() => {
        if (initialContactData) {
            setFormData({
                ...initialContactData,
                id: '',
                createdAt: new Date()
            });
            const initialProps = Object.entries(initialContactData.props).map(([key, value]) => ({ key, value }));
            setNewProps(initialProps);
        }
    }, [initialContactData]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({ ...prevState, [name]: value }));
    };

    const handleTagsChange = (selectedOptions: any) => {
        const tags = selectedOptions ? selectedOptions.map((option: any) => option.value) : [];
        setFormData(prevState => ({ ...prevState, tags }));
    };

    const handlePropsChange = (index: number, selectedOption: any, value: string) => {
        const updatedProps = [...newProps];
        const key = selectedOption ? selectedOption.value : updatedProps[index].key;
        updatedProps[index] = { key, value };
        setNewProps(updatedProps);
    };

    const removePropsField = (index: number) => {
        const updatedProps = [...newProps];
        updatedProps.splice(index, 1);
        setNewProps(updatedProps);
    };

    const handleSave = async () => {
        const finalProps = newProps.reduce((acc, { key, value }) => {
            if (key) acc[key] = value;
            return acc;
        }, {} as { [key: string]: string });

        const updatedFormData = { ...formData, props: finalProps };

        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
                body: JSON.stringify(updatedFormData),
            });

            const textResponse = await res.text();
            console.log('Raw response:', textResponse);

            if (!res.ok) {
                throw new Error(`Error saving contact: ${res.statusText}`);
            }

            setShowPopup(false);
            if (onClose) onClose(); // Call onClose prop
            toast.success("Contact added successfully!");
            onSave();
            router.refresh();
        } catch (error) {
            toast.error("Failed to add contact.");
            console.error('Failed to add contact:', error);
        }
    };

    const addNewPropsField = () => {
        setNewProps([...newProps, { key: '', value: '' }]);
    };

    const getFilteredPropsOptions = () => {
        return availablePropsKeys.filter(key => !newProps.some(prop => prop.key === key)).map(key => ({ label: key, value: key }));
    };

    const openPopup = async () => {
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${tenantUniqueName}`, {
                headers: {
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
            });

            if (!res.ok) {
                throw new Error(`Error fetching last contact: ${res.statusText}`);
            }

            const contacts = await res.json();
            let propsArray = [{ key: '', value: '' }];
            if (contacts && contacts.length > 0) {
                const lastContact = contacts[contacts.length - 1];
                if (lastContact.props) {
                    propsArray = Object.entries(lastContact.props).map(([key, value]) => ({ key, value: '' }));
                }
            }

            setFormData({
                id: '',
                title: '',
                user: '',
                tenantUniqueName: tenantUniqueName,
                comments: '',
                tags: [],
                props: {},
                createdAt: new Date(),
                attributesToString: ""
            });
            setNewProps(propsArray);
            setShowPopup(true);
        } catch (error) {
            console.error('Failed to fetch last contact props:', error);
        }
    };

    useEffect(() => {
        if (initialContactData) {
            setShowPopup(true);
        }
    }, [initialContactData]);

    return (
        <div>
            <button onClick={openPopup}
                    className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                Add new Contact
                <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faPlus}/>
            </button>

            {showPopup && (
                <div
                    className="absolute z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-60 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-3xl w-full my-10 overflow-auto">
                        <h2 className="font-semibold mb-4 text-2xl">Add New Contact</h2>
                        <form>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="title">
                                    Title
                                </label>
                                <input
                                    type="text"
                                    id="title"
                                    name="title"
                                    value={formData.title}
                                    onChange={handleChange}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="comments">
                                    Comments
                                </label>
                                <textarea
                                    id="comments"
                                    name="comments"
                                    value={formData.comments}
                                    onChange={handleChange}
                                    className="shadow appearance-none border rounded w-full min-h-32 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="tags">
                                    Tags
                                </label>
                                <CreatableSelect
                                    id="tags"
                                    name="tags"
                                    isMulti
                                    options={availableTags}
                                    onChange={handleTagsChange}
                                    value={formData.tags.map(tag => ({ label: tag, value: tag }))}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2">
                                    Properties
                                </label>
                                {newProps.map((prop, index) => (
                                    <div key={index} className="flex items-center mb-3">
                                        <CreatableSelect
                                            value={{ label: prop.key, value: prop.key }}
                                            onChange={(selectedOption) => handlePropsChange(index, selectedOption, prop.value)}
                                            options={getFilteredPropsOptions()}
                                            className="flex-1 mr-2"
                                            isClearable
                                            isSearchable
                                            placeholder="Select key"
                                        />
                                        <input
                                            type="text"
                                            value={prop.value}
                                            onChange={(e) => handlePropsChange(index, { value: prop.key }, e.target.value)}
                                            className="flex-1 shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                        />
                                        <button
                                            type="button"
                                            onClick={() => removePropsField(index)}
                                            className="ml-2 text-gray-500 hover:text-gray-700 focus:outline-none"
                                        >
                                            <FontAwesomeIcon icon={faTimes} />
                                        </button>
                                    </div>
                                ))}
                                <button type="button" onClick={addNewPropsField}
                                        className="mt-2 px-4 py-1 border-1px text-primary-light border-primary-light rounded-8 font-normal hover:scale-105 transition">
                                    Add Property
                                </button>
                            </div>
                            <div className="mt-4 flex justify-center items-center">
                                <button onClick={() => { setShowPopup(false); if (onClose) onClose(); }}
                                        className="mt-4 mx-1 px-4 py-1 bg-danger text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                    Close Popup
                                </button>
                                <button type="button" onClick={handleSave}
                                        className="mt-4 mx-1 px-4 py-1 bg-primary-light text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Add Contact
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AddNewContactPopup;
