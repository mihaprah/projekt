import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Contact as ContactModel } from '../../models/Contact';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faTrash,
    faChevronLeft,
    faChevronRight,
    faInfo,
    faTags,
    faCopy,
    faExclamationTriangle, faPlus, faMinus, faRotateLeft
} from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Tenant } from "@/models/Tenant";
import ContactExportPopup from "@/Components/Contact/ContactExportPopup";
import Select, { MultiValue } from 'react-select';
import CreatableSelect from "react-select/creatable";
import AddNewContactPopup from "@/Components/Contact/AddNewContactPopup";
import AddPropsPopup from "@/Components/Contact/AddPropsPopup";
import RemovePropsPopup from "@/Components/Contact/RemovePropsPopup"; // Import the AddPropsPopup component

interface ContactsProps {
    contacts: ContactModel[];
    tenantUniqueName: string;
    tenantId: string;
    IdToken: string;
    view: 'grid' | 'list';
    onChange: () => void;
    tenant: Tenant;
    deleted?: boolean;
}

interface TagOption {
    label: string;
    value: string;
}

const Contacts: React.FC<ContactsProps> = ({
                                               contacts,
                                               tenantUniqueName,
                                               tenantId,
                                               IdToken,
                                               view,
                                               onChange,
                                               tenant,
                                               deleted
                                           }) => {
    const router = useRouter();
    const [selectedContacts, setSelectedContacts] = useState<string[]>([]);
    const [selectAll, setSelectAll] = useState(false);
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [contactToDelete, setContactToDelete] = useState<string | null>(null);
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [currentPage, setCurrentPage] = useState(1);
    const [contactsPerPage, setContactsPerPage] = useState(50);
    const [showTagConfirmation, setShowTagConfirmation] = useState(false);
    const [showPropConfirmation, setShowPropConfirmation] = useState(false);
    const [selectedTags, setSelectedTags] = useState<TagOption[]>([]);
    const [showAllTags, setShowAllTags] = useState<string | null>(null);
    const [duplicateContact, setDuplicateContact] = useState<ContactModel | null>(null);
    const [contactTitle, setContactTitle] = useState('');
    const [confirmationText, setConfirmationText] = useState("");
    const [isDeleteDisabled, setIsDeleteDisabled] = useState(true);
    const [showRemoveTagConfirmation, setShowRemoveTagConfirmation] = useState(false);
    const [showRemovePropConfirmation, setShowRemovePropConfirmation] = useState(false);
    const [showRevertConfirmation, setShowRevertConfirmation] = useState(false);
    const [revertContact, setRevertContact] = useState<string | null>(null);

    const handleViewDetails = (contactId: string, tenantUniqueName: string) => {
        router.push(`/contacts/${tenantUniqueName}/${contactId}`);
    };

    useEffect(() => {
        const savedViewMode = localStorage.getItem('viewMode');
        if (savedViewMode === 'list' || savedViewMode === 'grid') {
            setViewMode(savedViewMode as 'list' | 'grid');
        }
        if(deleted){
            setViewMode('list');
        }

    }, [view, contacts]);

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
        if(!deleted){
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${contactId}/${tenantUniqueName}`, {
                    method: 'DELETE',
                    headers: {
                        'userToken': `Bearer ${IdToken}`,
                    },
                });

                if (!res.ok) {
                    toast.error(res.statusText || 'Failed to delete contact');
                }
                toast.success('Contact deleted successfully');
                onChange();
                router.refresh();
            } catch (error: any) {
                toast.error(error.message || 'Failed to delete contact');
            }
        } else {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/delete/${contactId}/${tenantUniqueName}`, {
                    method: 'DELETE',
                    headers: {
                        'userToken': `Bearer ${IdToken}`,
                    },
                });

                if (!res.ok) {
                    toast.error(res.statusText || 'Failed to delete contact');
                }
                toast.success('Contact permanently deleted successfully');
                onChange();
                router.refresh();
            } catch (error: any) {
                toast.error(error.message || 'Failed to delete contact permanently');
            }
        }

    };

    const confirmDelete = (contactId: string, title?: string) => {
        setContactToDelete(contactId);
        setShowConfirmation(true);
        if(title){
            setContactTitle(title);
        }
    };

    const handleRevert = async (contactId: string) => {
        setShowRevertConfirmation(false);
        setRevertContact(null);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/revert/${contactId}/${tenantUniqueName}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                }
            });

            if (!res.ok) {
                toast.error(res.statusText || 'Failed to revert contact');
            }
            toast.success('Contact reverted successfully');
            onChange();
            router.refresh();
        } catch (error: any) {
            toast.error(error.message || 'Failed to revert contact');
        }

    }

    const confirmRevert = (contactId: string, title?: string) => {
        setRevertContact(contactId);
        setShowRevertConfirmation(true);
        if(title){
            setContactTitle(title);
        }
    }

    const handleAddTags = async () => {
        setShowTagConfirmation(false);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/tags/multiple/add/${tenantUniqueName}/${selectedTags.map(tag => tag.value)}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                    'tenantId': tenantId
                },
                body: JSON.stringify(selectedContacts)
            });

            if (!res.ok) {
                toast.error(res.statusText || 'Failed to add tags');
            }

            toast.success('Tags added successfully');
            setSelectedContacts([]);
            setSelectedTags([]);
            router.refresh();
        } catch (error: any) {
            toast.error(error.message || 'Failed to add tags');
        }
    };

    const handleRemoveTags = async () => {
        setShowTagConfirmation(false);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/tags/multiple/remove/${tenantUniqueName}/${selectedTags.map(tag => tag.value)}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                    'tenantId': tenantId
                },
                body: JSON.stringify(selectedContacts)
            });

            if (!res.ok) {
                toast.error(res.statusText || 'Failed to remove tags');
            }

            toast.success('Tags removed successfully');
            setSelectedContacts([]);
            setSelectedTags([]);
            router.refresh();
        } catch (error: any) {
            toast.error(error.message || 'Failed to remove tags');
        }
    };


    const handleAddProps = () => {
        setShowPropConfirmation(true);
    };

    const handleRemoveProps = () => {
        setShowRemovePropConfirmation(true);
    };


    const handleTagChange = (newValue: MultiValue<TagOption>) => {
        setSelectedTags(newValue as TagOption[]);
    };

    const handleConfirmationTextChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmationText(event.target.value);
        setIsDeleteDisabled(event.target.value !== contactTitle);
    };

    const handleDuplicateContact = (contact: ContactModel) => {
        setDuplicateContact({
            ...contact,
            title: `${contact.title} - duplicate`
        });
    };

    const indexOfLastContact = currentPage * contactsPerPage;
    const indexOfFirstContact = indexOfLastContact - contactsPerPage;
    const currentContacts = contacts.slice(indexOfFirstContact, indexOfLastContact);

    const paginate = (pageNumber: number) => setCurrentPage(pageNumber);
    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <div className="flex items-center space-x-2">
                    <label htmlFor="contactsPerPage" className="mr-2">Show:</label>
                    <select
                        id="contactsPerPage"
                        value={contactsPerPage}
                        onChange={(e) => setContactsPerPage(Number(e.target.value))}
                        className="form-select"
                    >
                        <option value={10}>10</option>
                        <option value={25}>25</option>
                        <option value={50}>50</option>
                        <option value={100}>100</option>
                    </select>
                    {selectedContacts.length > 0 && !deleted && (
                        <div>
                            <button
                                onClick={() => setShowTagConfirmation(true)}
                                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                                Add Tags <FontAwesomeIcon className="mr-1" icon={faTags}/>
                            </button>
                            <button
                                onClick={() => setShowRemoveTagConfirmation(true)}
                                className="btn px-4 btn-sm bg-red-600 border-0 text-white dark:bg-red-700 dark:hover:bg-red-700 rounded-8 font-semibold hover:scale-105 transition hover:bg-red-700 ml-2 mr-5">
                                Remove Tags <FontAwesomeIcon className="mr-1" icon={faTags}/>
                            </button>
                            <button
                                onClick={handleAddProps}
                                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark ml-2">
                                Add Prop <FontAwesomeIcon className="mr-1" icon={faPlus}/>
                            </button>
                            <button
                                onClick={handleRemoveProps}
                                className="btn px-4 btn-sm bg-red-600 border-0 text-white dark:bg-red-700 dark:hover:bg-red-700 rounded-8 font-semibold hover:scale-105 transition hover:bg-red-700 ml-2">
                                Remove Props <FontAwesomeIcon className="mr-1" icon={faMinus}/>
                            </button>

                        </div>
                    )}
                </div>
                <div className="flex items-center">
                    {!deleted && (
                        <ContactExportPopup IdToken={IdToken} tenantUniqueName={tenantUniqueName} tenantId={tenantId}
                                            contactIds={selectedContacts}/>)}
                </div>
            </div>

            {viewMode === 'grid' ? (
                <div>
                    <div className="flex items-center mb-3">
                        <input
                            type="checkbox"
                            checked={selectAll}
                            onChange={handleSelectAll}
                            className="form-checkbox h-5 w-5 text-primary-light transition duration-150 ease-in-out rounded-8"
                        />
                        <span className="ml-2 text-xl">Select all</span>
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-1">
                        {currentContacts.map(contact => (
                            <div
                                key={contact.id}
                                className="border p-4 rounded-lg shadow-md hover:shadow-xl transition-shadow duration-200 relative flex flex-col cursor-pointer"
                                onClick={() => handleViewDetails(contact.id, contact.tenantUniqueName)}
                            >
                                <div className="flex justify-between items-start mb-5">
                                    <div className="flex items-center">
                                        <input
                                            type="checkbox"
                                            checked={selectedContacts.includes(contact.id)}
                                            onClick={(e) => e.stopPropagation()}
                                            onChange={(e) => {
                                                handleSelectContact(contact.id);
                                            }}
                                            className="form-checkbox h-7 w-7 text-primary-light transition duration-150 ease-in-out rounded-8"
                                        />
                                    </div>
                                    <div className="flex items-center">
                                        <button
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleDuplicateContact(contact);
                                            }}
                                            className="text-primary-light hover:text-primary-dark transition mr-4">
                                            <FontAwesomeIcon icon={faCopy} className="mr-2 w-5 h-5"/>
                                        </button>
                                        <button
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                confirmDelete(contact.id);
                                            }}
                                            className="text-red-600 hover:text-red-800 transition">
                                            <FontAwesomeIcon className="w-5 h-5" icon={faTrash}/>
                                        </button>
                                    </div>
                                </div>
                                <div className="text-center mb-6">
                                    {contact.title && (
                                        <div className={"flex justify-center"}>
                                            {contact.props.prefix && tenant.displayProps.includes('prefix') && (
                                                <p className="text-xl font-bold mr-1">{contact.props.prefix}</p>
                                            )}
                                            <h2 className="text-xl font-bold mb-1.5">{contact.title}</h2>
                                        </div>
                                    )}
                                    {contact.props.email && tenant.displayProps.includes('email') && (
                                        <a href={`mailto:${contact.props.email}`}
                                           onClick={(e) => e.stopPropagation()}
                                           className="block mb-1.5 text-primary-light hover:underline">
                                            {contact.props.email}
                                        </a>
                                    )}
                                    {contact.props.phoneNumber && tenant.displayProps.includes('phoneNumber') && (
                                        <p className="mb-1.5">{contact.props.phoneNumber}</p>
                                    )}
                                </div>

                                    <div className="grid grid-cols-2 gap-x-4 gap-y-2 mb-6 text-center justify-center">
                                        {tenant.displayProps.filter(prop => prop !== 'prefix' && prop !== 'email' && prop !== 'phoneNumber' && contact.props[prop]).map(prop => (
                                            <div key={prop}>
                                                <strong>{tenant.labels[prop]}</strong>
                                                <p>{contact.props[prop]}</p>
                                            </div>
                                        ))}
                                    </div>
                                    <div className="mt-auto flex justify-center flex-wrap gap-2">
                                        {contact.tags && contact.tags.slice(0, showAllTags === contact.id ? contact.tags.length : 3).map((tag, index) => (
                                            <span key={index}
                                                  className="bg-white text-primary-light border border-primary-light text-sm font-medium px-3 py-1.5 rounded-8">
                                {tag}
                            </span>
                                        ))}
                                        {contact.tags && contact.tags.length > 3 && (
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    setShowAllTags(showAllTags === contact.id ? null : contact.id);
                                                }}
                                                className="text-primary-light text-sm font-medium px-2 py-1 rounded-8 mt-1">
                                                {showAllTags === contact.id ? 'Show less' : `+${contact.tags.length - 3} more`}
                                            </button>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full bg-white table-auto">
                            <thead className={"border-b-2 border-gray-300"}>
                            <tr>
                                {!deleted && (
                                    <th className="px-2 py-2 border-b-2 border-gray-300 text-left text-xs leading-4 text-gray-600 tracking-wider">
                                        <div className="flex items-center w-5 h-5">
                                            <input
                                                type="checkbox"
                                                checked={selectAll}
                                                onChange={handleSelectAll}
                                                className="form-checkbox h-4 w-4 hover:scale-105 text-primary-light transition duration-150 ease-in-out"
                                            />
                                        </div>
                                    </th>
                                )}
                                <th className="px-2 py-2 border-b-2 border-gray-300 text-left text-sm leading-4 text-gray-600 tracking-wider">
                                    Title
                                </th>
                                {tenant.displayProps.filter(prop => prop !== 'prefix').map(prop => (
                                    <th key={prop}
                                        className="px-2 py-2 border-b-2 border-gray-300 text-left text-sm leading-4 text-gray-600 tracking-wider">
                                        {tenant.labels[prop]}
                                    </th>
                                ))}
                                <th className="px-2 py-2 border-b-2 border-gray-300 text-left text-xs leading-4 text-gray-600 tracking-wider"></th>
                            </tr>
                            </thead>
                            <tbody>
                            {currentContacts.map(contact => (
                                <tr key={contact.id} className={"hover:bg-gray-100"}>
                                    {!deleted && (
                                    <td className="px-2 py-3 whitespace-no-wrap border-b border-gray-300">
                                        <input
                                            type="checkbox"
                                            checked={selectedContacts.includes(contact.id)}
                                            onChange={() => handleSelectContact(contact.id)}
                                            className="form-checkbox h-4 w-4 hover:scale-105 text-primary-light transition duration-150 ease-in-out"
                                        />
                                    </td> )}
                                    <td className={"px-2 py-3 whitespace-no-wrap border-b border-gray-300"}>
                                        {contact.props.prefix && tenant.displayProps.includes('prefix') && (
                                            <span className="mr-2">{contact.props.prefix}</span>
                                        )}
                                        {contact.title}
                                    </td>
                                    {tenant.displayProps.filter(prop => prop !== 'prefix').map(prop => (
                                        <td key={prop} className="px-2 py-3 whitespace-no-wrap border-b border-gray-300">
                                            <div className="text-sm leading-4 text-gray-800">
                                                {prop === 'email' ? (
                                                    <a href={`mailto:${contact.props[prop]}`} className="text-primary-light hover:underline">
                                                        {contact.props[prop]}
                                                    </a>
                                                ) : (
                                                    contact.props[prop]
                                                )}
                                            </div>
                                        </td>
                                    ))}

                                <td className="px-2 py-3 whitespace-no-wrap border-b border-gray-300">
                                    <div className="flex flex-wrap gap-1">
                                        {contact.tags && contact.tags.slice(0, showAllTags === contact.id ? contact.tags.length : 3).map((tag, index) => (
                                            <span key={index}
                                                  className="bg-white text-primary-light border border-primary-light text-xs font-medium px-2 py-1 rounded-8">
                                    {tag}
                                </span>
                                        ))}
                                        {contact.tags && contact.tags.length > 3 && (
                                            <button
                                                onClick={() => setShowAllTags(showAllTags === contact.id ? null : contact.id)}
                                                className="text-primary-light text-xs font-medium px-2 py-1 rounded-8 mt-1">
                                                {showAllTags === contact.id ? 'Show less' : `+${contact.tags.length - 3} more`}
                                            </button>
                                        )}
                                    </div>
                                </td>
                                    {!deleted ? (
                                        <td className="px-2 py-4 whitespace-no-wrap border-b border-gray-300 text-right flex items-center">
                                            <button
                                                onClick={() => handleViewDetails(contact.id, contact.tenantUniqueName)}
                                                className="text-primary-light hover:text-primary-dark transition relative group">
                                                <FontAwesomeIcon icon={faInfo} className="mr-2"/>
                                            </button>
                                            <button
                                                onClick={() => handleDuplicateContact(contact)}
                                                className="text-primary-light hover:text-primary-dark transition ml-4">
                                                <FontAwesomeIcon icon={faCopy} className="mr-2"/>
                                            </button>
                                            <button
                                                onClick={() => confirmDelete(contact.id)}
                                                className="text-red-600 hover:text-red-800 transition ml-4">
                                                <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faTrash}/>
                                            </button>
                                        </td>
                                    ) : (
                                        <td className="px-2 py-4 whitespace-no-wrap border-b border-gray-300 text-right flex items-center">
                                            <button
                                                onClick={() => confirmDelete(contact.id, contact.title)}
                                                className="text-red-600 hover:text-red-800 transition ml-4">
                                                Delete permanently<FontAwesomeIcon className="ml-2 w-3.5 h-auto"
                                                                                   icon={faTrash}/>
                                            </button>
                                            <button
                                                onClick={() => confirmRevert(contact.id, contact.title)}
                                                className="text-blue-600 hover:text-blue-800 transition ml-4">
                                                Revert<FontAwesomeIcon className="ml-2 w-3.5 h-auto"
                                                                                   icon={faRotateLeft}/>
                                            </button>
                                        </td>
                                    )}
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

            )}

            <div className="flex justify-between items-center my-4">
                <button
                    onClick={() => paginate(currentPage - 1)}
                    disabled={currentPage === 1}
                    className="px-4 py-1 rounded-8 bg-gray-300 text-black mr-2 disabled:opacity-50 disabled:cursor-not-allowed hover:scale-105 transition">
                    <FontAwesomeIcon className={"w-2.5 h-auto mr-1"} icon={faChevronLeft}/> Previous
                </button>
                <span
                    className="text-md">{`Page ${currentPage} of ${Math.ceil(contacts.length / contactsPerPage)}`}</span>
                <button
                    onClick={() => paginate(currentPage + 1)}
                    disabled={currentPage === Math.ceil(contacts.length / contactsPerPage)}
                    className="px-4 py-1 rounded-8 bg-gray-300 text-black ml-2 disabled:opacity-50 disabled:cursor-not-allowed hover:scale-105 transition">
                    Next <FontAwesomeIcon className={"w-2.5 h-auto ml-1"} icon={faChevronRight}/>
                </button>
            </div>


            {showConfirmation && deleted && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-8 rounded-lg shadow-lg relative z-50">
                        <h2 className="text-xl mb-4 font-semibold flex items-center">
                            Are you sure you want to delete this contact?
                            <FontAwesomeIcon className="ml-2 w-5 h-5 text-red-600" icon={faExclamationTriangle}/>
                        </h2>
                        <p>
                            This action will permanently delete the contact and all its data from the database.<br/>
                            No users will be able to access this contact anymore.
                        </p>
                        <p className={"mt-3"}>Type <strong>{contactTitle}</strong> to confirm.</p>
                        <input
                            type="text"
                            className="mt-2 mb-4 p-2 border rounded w-full"
                            value={confirmationText}
                            onChange={handleConfirmationTextChange}
                        />
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowConfirmation(false)}
                                className="px-4 py-1 rounded-8 bg-gray-300 font-semibold text-black mr-2 disabled:opacity-50 hover:scale-105 transition">
                                Cancel
                            </button>
                            <button
                                onClick={() => handleDelete(contactToDelete!)}
                                className="btn px-4 py-1 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:scale-105 transition hover:bg-danger"
                                disabled={isDeleteDisabled}>
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showConfirmation && !deleted && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-8 rounded-lg shadow-lg">
                        <h2 className="text-xl mb-4">Are you sure you want to delete this contact?</h2>
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowConfirmation(false)}
                                className="px-4 py-1 rounded-8 hover:scale-105 transition font-semibold bg-gray-300 text-black mr-2">
                                Cancel
                            </button>
                            <button
                                onClick={() => handleDelete(contactToDelete!)}
                                className="px-4 py-1 font-semibold bg-danger hover:scale-105 transition rounded-8 text-white">
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showRevertConfirmation && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-8 rounded-lg shadow-lg">
                        <h2 className="text-xl mb-4">Are you sure you want to revert contact <b>{contactTitle}</b>?</h2>
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowRevertConfirmation(false)}
                                className="px-4 py-1 rounded-8 hover:scale-105 transition font-semibold bg-gray-300 text-black mr-2">
                                Cancel
                            </button>
                            <button
                                onClick={() => handleRevert(revertContact!)}
                                className="px-4 py-1 font-semibold bg-primary-light hover:scale-105 transition rounded-8 text-white">
                                Revert
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showTagConfirmation && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-8 rounded-lg shadow-lg">
                        <h2 className="text-xl mb-4">Add tags to selected contacts</h2>
                        <CreatableSelect
                            isMulti
                            options={Object.entries(tenant.contactTags).map(([key, value]) => ({
                                label: key,
                                value: key
                            }))}
                            value={selectedTags}
                            onChange={handleTagChange}
                            className="mb-4"
                        />
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowTagConfirmation(false)}
                                className="btn px-4 btn-sm bg-red-600 border-0 text-white rounded-8 font-semibold hover:scale-105 transition hover:bg-red-700 mr-5">
                                Close popup
                            </button>
                            <button
                                onClick={handleAddTags}
                                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                                Add Tags
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showRemoveTagConfirmation && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-8 rounded-lg shadow-lg">
                        <h2 className="text-xl mb-4">Remove tags from selected contacts</h2>
                        <Select
                            isMulti
                            options={Object.entries(tenant.contactTags).map(([key, value]) => ({
                                label: key,
                                value: key
                            }))}
                            value={selectedTags}
                            onChange={handleTagChange}
                            className="mb-4"
                        />
                        <div className="flex justify-end">
                            <button
                                onClick={() => setShowRemoveTagConfirmation(false)}
                                className="btn px-4 btn-sm bg-red-600 border-0 text-white rounded-8 font-semibold hover:scale-105 transition hover:bg-red-700 mr-5">
                                Close popup
                            </button>
                            <button
                                onClick={handleRemoveTags}
                                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                                Remove Tags
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showPropConfirmation && (
                <AddPropsPopup
                    tenantUniqueName={tenantUniqueName}
                    selectedContacts={selectedContacts}
                    IdToken={IdToken}
                    tenantId={tenantId}
                    availableProps={Object.entries(tenant.labels).map(([key, value]) => ({ label: value, value: key }))}
                    onClose={() => setShowPropConfirmation(false)}
                    onSave={() => {
                        setShowPropConfirmation(false);
                        onChange();
                    }}
                />
            )}

            {showRemovePropConfirmation && (
                <RemovePropsPopup
                    tenantUniqueName={tenantUniqueName}
                    selectedContacts={selectedContacts}
                    IdToken={IdToken}
                    tenantId={tenantId}
                    availableProps={Object.entries(tenant.labels).map(([key, value]) => ({ label: value, value: key }))}
                    onClose={() => setShowRemovePropConfirmation(false)}
                    onSave={() => {
                        setShowRemovePropConfirmation(false);
                        onChange();
                    }}
                />
            )}

            {duplicateContact && (
                <div className="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-65">
                <AddNewContactPopup
                    tenantUniqueName={tenantUniqueName}
                    onSave={() => {
                        setDuplicateContact(null);
                        onChange();
                    }}
                    initialContactData={duplicateContact}
                    onClose={() => setDuplicateContact(null)}

                /></div>
            )}
        </div>
    );
};

export default Contacts;
