"use client";

import React, { useState } from 'react';
import { Contact as ContactModel } from '../../models/Contact';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit } from '@fortawesome/free-solid-svg-icons';

interface EditContactPopupProps {
    contact: ContactModel;
}

const EditContactPopup: React.FC<EditContactPopupProps> = ({ contact }) => {
    const [showPopup, setShowPopup] = useState(false);
    const [formData, setFormData] = useState(contact);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({ ...prevState, [name]: value }));
    };

    const handlePropsChange = (key: string, value: string) => {
        setFormData(prevState => ({
            ...prevState,
            props: { ...prevState.props, [key]: value }
        }));
    };

    const handleSave = async () => {
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${document.cookie.split('IdToken=')[1]}`,
                },
                body: JSON.stringify(formData),
            });

            if (!res.ok) {
                throw new Error(`Error saving contact: ${res.statusText}`);
            }

            const updatedContact = await res.json();
            console.log('Contact updated:', updatedContact);

            setShowPopup(false);
            window.location.reload();
        } catch (error) {
            console.error('Failed to save contact:', error);
        }
    };

    return (
        <div>
            <button onClick={() => setShowPopup(true)}
                    className="btn mt-2 px-6 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                Edit Contact <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faEdit} />
            </button>

            {showPopup && (
                <div className="absolute z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-60 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-3xl w-full">
                        <h2 className="font-semibold mb-4 text-2xl">Edit Contact</h2>
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
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="user">
                                    User
                                </label>
                                <input
                                    type="text"
                                    id="user"
                                    name="user"
                                    value={formData.user}
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
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="tags">
                                    Tags
                                </label>
                                <input
                                    type="text"
                                    id="tags"
                                    name="tags"
                                    value={formData.tags.join(', ')}
                                    onChange={(e) => setFormData(prevState => ({ ...prevState, tags: e.target.value.split(',').map(tag => tag.trim()) }))}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            {Object.entries(formData.props).map(([key, value]) => (
                                <div key={key} className="mb-4">
                                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor={key}>
                                        {key}
                                    </label>
                                    <input
                                        type="text"
                                        id={key}
                                        name={key}
                                        value={value}
                                        onChange={(e) => handlePropsChange(key, e.target.value)}
                                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    />
                                </div>
                            ))}
                            <div className="mt-4 flex justify-center items-center">
                                <button onClick={() => setShowPopup(false)}
                                        className="btn mt-4 mx-3 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                    Close Popup
                                </button>
                                <button type="button" onClick={handleSave}
                                        className="btn mt-4 mx-3 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Save
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditContactPopup;
