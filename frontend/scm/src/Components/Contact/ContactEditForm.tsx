"use client";

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useSWRConfig } from 'swr';
import { Contact as ContactModel } from '../../models/Contact';

interface EditContactFormProps {
    contact: ContactModel;
}

const EditContactForm: React.FC<EditContactFormProps> = ({ contact }) => {
    const [formData, setFormData] = useState(contact);
    const router = useRouter();
    const { mutate } = useSWRConfig();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({ ...prevState, [name]: value }));
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

            // Invalidate SWR cache for contacts
            mutate(`/contacts/${formData.tenantUniqueName}`);

            // Redirect to contacts list
            router.push(`/contacts/${formData.tenantUniqueName}`);
        } catch (error) {
            console.error('Failed to save contact:', error);
        }
    };

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-3xl font-bold">Edit Contact</h1>
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
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="tenantUniqueName">
                        Tenant Unique Name
                    </label>
                    <input
                        type="text"
                        id="tenantUniqueName"
                        name="tenantUniqueName"
                        value={formData.tenantUniqueName}
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
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="props">
                        Props
                    </label>
                    <textarea
                        id="props"
                        name="props"
                        value={JSON.stringify(formData.props, null, 2)}
                        onChange={(e) => setFormData(prevState => ({ ...prevState, props: JSON.parse(e.target.value) }))}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    />
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="attributesToString">
                        Attributes to String
                    </label>
                    <textarea
                        id="attributesToString"
                        name="attributesToString"
                        value={formData.attributesToString}
                        onChange={handleChange}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    />
                </div>
                <button
                    type="button"
                    onClick={handleSave}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                >
                    Save
                </button>
            </form>
        </div>
    );
};

export default EditContactForm;
