"use client";

import React, { useState, useEffect } from 'react';
import { Contact as ContactModel } from '../../models/Contact';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import CreatableSelect from 'react-select/creatable';
import { toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { useRouter } from "next/navigation";

interface EditContactPopupProps {
    contact: ContactModel;
    tenantUniqueName: string;
}

const EditContactPopup: React.FC<EditContactPopupProps> = ({ contact, tenantUniqueName }) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [formData, setFormData] = useState(contact);

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
                    toast.error(res.statusText || 'Failed to fetch tags');
                }

                const tenant = await res.json();
                const tags = Object.keys(tenant.contactTags).map(tag => ({ label: tag, value: tag }));
                setAvailableTags(tags);
            } catch (error: any) {
                toast.error(error.message || 'Failed to fetch tags');
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
                    toast.error(res.statusText || 'Failed to fetch props keys');
                }

                const tenant = await res.json();
                if (tenant) {
                    const propsKeys = Object.keys(tenant.labels);
                    setAvailablePropsKeys(propsKeys);
                }
            } catch (error: any) {
                toast.error(error.message || 'Failed to fetch props keys');
            }
        };

        fetchTags();
        fetchPropsKeys();

        // Initialize newProps from existing formData.props
        const propsArray = Object.entries(contact.props).map(([key, value]) => ({ key, value }));
        setNewProps([...propsArray, { key: '', value: '' }]);
    }, [contact, tenantUniqueName]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({ ...prevState, [name]: value }));
    };

    const handleTagsChange = (selectedOptions: any) => {
        const tags = selectedOptions ? selectedOptions.map((option: any) => option.value) : [];
        setFormData(prevState => ({ ...prevState, tags }));
    };

    const handleKeyChange = (index: number, selectedOption: any) => {
        const updatedProps = [...newProps];
        const key = selectedOption ? selectedOption.value : updatedProps[index].key;
        updatedProps[index] = { key, value: updatedProps[index].value };
        setNewProps(updatedProps);
    };

    const handleValueChange = (index: number, value: string) => {
        const updatedProps = [...newProps];
        updatedProps[index] = { key: updatedProps[index].key, value };
        setNewProps(updatedProps);
    };

    const addNewPropsField = () => {
        setNewProps([...newProps, { key: '', value: '' }]);
    };

    const removePropsField = (index: number) => {
        const updatedProps = [...newProps];
        updatedProps.splice(index, 1);
        setNewProps(updatedProps);
    };

    const getFilteredPropsOptions = () => {
        return availablePropsKeys.filter(key => !newProps.some(prop => prop.key === key)).map(key => ({ label: key, value: key }));
    };

    const handleUpdate = async () => {
        if (formData.title.length < 3) {
            toast.error("Error! Title is too short");
            return
        }
        const finalProps = newProps.reduce((acc, { key, value }) => {
            if (key) acc[key] = value;
            return acc;
        }, {} as { [key: string]: string });

        setFormData(prevState => ({ ...prevState, props: finalProps }));

        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
                body: JSON.stringify({ ...formData, props: finalProps }),
            });

            if (!res.ok) {
                toast.error(res.statusText || "Failed to save contact.");
            }

            setShowPopup(false);
            toast.success("Contact saved successfully!");
            router.refresh();
        } catch (error: any) {
            toast.error(error.message || "Failed to save contact.");
        }
    };

    const handleDelete = async () => {
        setShowConfirmation(false);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${contact.id}/${tenantUniqueName}`, {
                method: 'DELETE',
                headers: {
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
            });

            if (!res.ok) {
                toast.error(res.statusText || "Failed to delete contact.");
            }
            toast.success('Contact deleted successfully');
            router.push(`/contacts/${tenantUniqueName}`);
            router.refresh();
        } catch (error: any) {
            toast.error(error.message ||'Failed to delete contact');
        }
    };

    return (
        <div>
            <div className="flex space-x-4">
                <button onClick={() => setShowPopup(true)}
                        className="btn mt-2 px-6 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                    Edit Contact <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faEdit} />
                </button>
                <button onClick={() => setShowConfirmation(true)}
                        className="btn mt-2 px-6 btn-sm bg-danger border-0 text-white dark:bg-danger dark:hover:bg-danger rounded-8 font-semibold hover:scale-105 transition hover:bg-danger">
                    Delete Contact <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faTrash} />
                </button>
            </div>

            {showPopup && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-3xl w-full my-10 overflow-auto">
                        <h2 className="font-semibold text-2xl">Edit Contact</h2>
                        <p className={"font-light text-sm mb-2"}>Edit an existing Contact, changes will be updated and can be viewed by all the group users. Attributes marked with * are required.</p>
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
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="user">
                                    User
                                </label>
                                <input
                                    type="text"
                                    id="user"
                                    name="user"
                                    value={formData.user}
                                    onChange={handleChange}
                                    disabled={true}
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
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline min-h-24"
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
                                    value={formData.tags.map(tag => ({label: tag, value: tag}))}
                                    options={availableTags}
                                    onChange={handleTagsChange}
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
                                            value={{label: prop.key, value: prop.key}}
                                            onChange={(selectedOption) => handleKeyChange(index, selectedOption)}
                                            options={getFilteredPropsOptions()}
                                            className="flex-1 mr-2"
                                            isClearable
                                            isSearchable
                                            placeholder="Select or create key"
                                        />
                                        <input
                                            type="text"
                                            value={prop.value}
                                            onChange={(e) => handleValueChange(index, e.target.value)}
                                            className="flex-1 shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                        />
                                        <button
                                            type="button"
                                            onClick={() => removePropsField(index)}
                                            className="ml-2 text-gray-500 hover:text-gray-700 focus:outline-none"
                                        >
                                            <FontAwesomeIcon icon={faTimes}/>
                                        </button>
                                    </div>
                                ))}
                                <button type="button" onClick={addNewPropsField}
                                        className="mt-2 px-4 py-1 border-1px text-primary-light border-primary-light rounded-8 font-normal hover:scale-105 transition">
                                    Add Property
                                </button>
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

            {showConfirmation && (
                <div className="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-75">
                <div className="bg-white p-8 rounded-lg shadow-lg">
                        <h2 className="text-xl mb-4">Are you sure you want to delete this contact?</h2>
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowConfirmation(false)}
                                className="rounded-8 px-4 py-2 font-semibold bg-gray-300 text-black mr-2 hover:scale-105 transition">
                                Cancel
                            </button>
                            <button
                                onClick={handleDelete}
                                className="px-4 bg-danger font-semibold rounded-8 py-2 text-white hover:scale-105 transition">
                                Delete <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faTrash} />
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditContactPopup;
