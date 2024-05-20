"use client";
import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Contact as ContactModel } from '../../models/Contact';
import { Tenant as TenantModel } from '../../models/Tenant';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTh, faList, faGear, faUsers, faTrash } from '@fortawesome/free-solid-svg-icons';
import EditTenantPopup from "@/Components/Tenant/EditTenantPopup";
import AddNewContactPopup from "@/Components/Contact/AddNewContactPopup";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface ContactsProps {
    contacts: ContactModel[];
    tenant: TenantModel;
    contactsNumber: number;
    tenantUniqueName: string;
    IdToken: string;
}

const Contacts: React.FC<ContactsProps> = ({ contacts, tenant, contactsNumber, tenantUniqueName, IdToken }) => {
    const router = useRouter();
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [selectedContacts, setSelectedContacts] = useState<string[]>([]);
    const [selectAll, setSelectAll] = useState(false);
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [contactToDelete, setContactToDelete] = useState<string | null>(null);

    useEffect(() => {
        // Check localStorage for the view mode preference
        const savedViewMode = localStorage.getItem('viewMode');
        if (savedViewMode === 'list' || savedViewMode === 'grid') {
            setViewMode(savedViewMode as 'list' | 'grid');
        }
    }, []);

    const handleViewDetails = (contactId: string, tenantUniqueName: string) => {
        router.push(`/contacts/${tenantUniqueName}/${contactId}`);
    };

    const toggleViewMode = () => {
        const newViewMode = viewMode === 'grid' ? 'list' : 'grid';
        setViewMode(newViewMode);
        localStorage.setItem('viewMode', newViewMode);
    };

    const handleSelectContact = (contactId: string) => {
        setSelectedContacts(prevSelected => {
            if (prevSelected.includes(contactId)) {
                return prevSelected.filter(id => id !== contactId);
            } else {
                return [...prevSelected, contactId];
            }
        });
    };

    const handleSelectAll = () => {
        if (selectAll) {
            setSelectedContacts([]);
        } else {
            setSelectedContacts(contacts.map(contact => contact.id));
        }
        setSelectAll(!selectAll);
    };

    const handleDelete = async (contactId: string) => {
        setShowConfirmation(false);
        setContactToDelete(null);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${contactId}/${tenantUniqueName}`, {
                method: 'DELETE',
                headers: {
                    'userToken': `Bearer ${IdToken}`,
                },
            });

            if (!res.ok) {
                throw new Error(`Error deleting contact: ${res.statusText}`);
            }
            toast.success('Contact deleted successfully');
            router.refresh();
        } catch (error) {
            toast.error('Failed to delete contact');
            console.error('Failed to delete contact:', error);
        }
    };

    const confirmDelete = (contactId: string) => {
        setContactToDelete(contactId);
        setShowConfirmation(true);
    };

    return (
        <div>
            <ToastContainer />
            <div className="flex justify-between mb-4">
                <div>
                    <div className={"flex items-center"}>
                        <h2 className="text-3xl font-semibold text-primary-light">{tenant.title}</h2>
                        <span className="ml-6 mr-2 text-2xl font-semibold ">{contactsNumber}</span>
                        <FontAwesomeIcon className="h-5 w-auto mb-0.5 " icon={faUsers}/>
                    </div>
                    <p className="text-m text-gray-600 mt-1 break-words max-w-96">{tenant.description}</p>
                    <div className="mt-4 flex items-center space-x-3">
                        <EditTenantPopup tenant={tenant}/>
                        <button type="button"
                                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                            Tenant Settings
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faGear}/>
                        </button>
                    </div>
                </div>
                <div className="flex items-end space-x-3">
                    <AddNewContactPopup tenantUniqueName={tenantUniqueName}/>
                    <button onClick={toggleViewMode}
                            className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        {viewMode === 'grid' ? (
                            <FontAwesomeIcon icon={faList}/>
                        ) : (
                            <FontAwesomeIcon icon={faTh}/>
                        )}
                    </button>
                </div>
            </div>

            {viewMode === 'grid' ? (
                <div>
                    <div className="flex items-center mb-2">
                        <input
                            type="checkbox"
                            checked={selectAll}
                            onChange={handleSelectAll}
                            className="form-checkbox h-5 w-5 text-primary-light transition duration-150 ease-in-out"
                        />
                        <span className="ml-2 text-xl">Select all</span>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {contacts.map(contact => (
                            <div key={contact.id} className="border p-4 rounded shadow relative flex flex-col">
                                <div className="flex items-center mb-2">
                                    <input
                                        type="checkbox"
                                        checked={selectedContacts.includes(contact.id)}
                                        onChange={() => handleSelectContact(contact.id)}
                                        className="form-checkbox h-5 w-5 text-primary-light transition duration-150 ease-in-out"
                                    />
                                    <h2 className="text-xl font-bold ml-2">{contact.title}</h2>
                                </div>
                                <p>{contact.props.Name}</p>
                                <button
                                    onClick={() => handleViewDetails(contact.id, contact.tenantUniqueName)}
                                    className="text-primary-light hover:text-primary-dark transition mt-2">
                                    Show Details
                                </button>
                                <button
                                    onClick={() => confirmDelete(contact.id)}
                                    className="absolute top-2 right-2 text-red-600 hover:text-red-800 transition">
                                    <FontAwesomeIcon icon={faTrash} />
                                </button>
                            </div>
                        ))}
                    </div>
                </div>


            ) : (
                <div className="overflow-x-auto">
                    <table className="min-w-full bg-white">
                        <thead>
                        <tr>
                            <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-gray-600 tracking-wider">
                                <div className="flex items-center">
                                    <input
                                        type="checkbox"
                                        checked={selectAll}
                                        onChange={handleSelectAll}
                                        className="form-checkbox h-5 w-5 text-primary-light transition duration-150 ease-in-out"
                                    />
                                    <span className="ml-2">Select all</span>
                                </div>
                            </th>
                            <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-gray-600 tracking-wider">Title</th>
                            <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-gray-600 tracking-wider">Name</th>
                            <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-gray-600 tracking-wider"></th>
                        </tr>
                        </thead>
                        <tbody>
                        {contacts.map(contact => (
                            <tr key={contact.id}>
                                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                                    <input
                                        type="checkbox"
                                        checked={selectedContacts.includes(contact.id)}
                                        onChange={() => handleSelectContact(contact.id)}
                                        className="form-checkbox h-5 w-5 text-primary-light transition duration-150 ease-in-out"
                                    />
                                </td>
                                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                                    <div className="text-sm leading-5 text-gray-800">{contact.title}</div>
                                </td>
                                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                                    <div className="text-sm leading-5 text-gray-800">{contact.props.Name}</div>
                                </td>
                                <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                                    <button
                                        onClick={() => handleViewDetails(contact.id, contact.tenantUniqueName)}
                                        className="text-primary-light hover:text-primary-dark transition">
                                        Show Details
                                    </button>
                                    <button
                                        onClick={() => confirmDelete(contact.id)}
                                        className="text-red-600 hover:text-red-800 transition ml-4">
                                        <FontAwesomeIcon icon={faTrash} />
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}

            {showConfirmation && (
                <div className="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-75">
                    <div className="bg-white p-8 rounded-lg shadow-lg">
                        <h2 className="text-xl mb-4">Are you sure you want to delete this contact?</h2>
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowConfirmation(false)}
                                className="btn bg-gray-300 text-black mr-2">
                                Cancel
                            </button>
                            <button
                                onClick={() => handleDelete(contactToDelete!)}
                                className="btn bg-red-600 text-white">
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Contacts;
