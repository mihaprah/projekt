"use client";
import React, { useState, ChangeEvent } from 'react';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUpload } from '@fortawesome/free-solid-svg-icons';
import 'react-toastify/dist/ReactToastify.css';

interface ImportContactsProps {
    tenantUniqueName: string;
    IdToken: string;
    onClose?: () => void;
}

const ImportContacts: React.FC<ImportContactsProps> = ({ tenantUniqueName, IdToken, onClose }) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [file, setFile] = useState<File | null>(null);

    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            setFile(e.target.files[0]);
        }
    };

    const handleImport = async () => {
        if (!file) {
            toast.error('Please select a file to upload');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('tenantUniqueName', tenantUniqueName);

        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/import`, {
                method: 'POST',
                headers: {
                    'userToken': `Bearer ${IdToken}`,
                },
                body: formData,
            });

            if (!res.ok) {
                throw new Error(`Error importing contacts: ${res.statusText}`);
            }

            setShowPopup(false);
            if (onClose) onClose();
            toast.success('Contacts imported successfully');
            router.refresh();
        } catch (error) {
            toast.error('Failed to import contacts');
            console.error('Failed to import contacts:', error);
        }
    };

    const openPopup = () => {
        setShowPopup(true);
    };

    return (
        <div>
            <button onClick={openPopup}
                    className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark ml-3">
                Import
                <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faUpload} />
            </button>

            {showPopup && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-3xl w-full my-10 overflow-auto">
                        <h2 className="font-semibold text-2xl">Import Contacts</h2>
                        <p className="font-light text-sm mb-2">Select a file to import contacts. Only .xlsx and .xls files are supported.</p>
                        <form>
                            <div className="mb-4">
                                <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="file">
                                    File*
                                </label>
                                <input
                                    type="file"
                                    id="file"
                                    accept=".xlsx, .xls"
                                    onChange={handleFileChange}
                                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                />
                            </div>
                            <div className="mt-4 flex justify-center items-center">
                                <button onClick={() => {
                                    setShowPopup(false);
                                    if (onClose) onClose();
                                }}
                                        className="mt-4 mx-1 px-4 py-1 bg-danger text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                    Close Popup
                                </button>
                                <button type="button" onClick={handleImport}
                                        className="mt-4 mx-1 px-4 py-1 bg-primary-light text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Import Contacts
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ImportContacts;
