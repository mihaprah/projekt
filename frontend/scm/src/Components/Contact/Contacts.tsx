"use client"
import React from 'react';
import { useRouter } from 'next/navigation';
import { Contact as ContactModel } from '../../models/Contact';

interface ContactsProps {
    contacts: ContactModel[];
}

const Contacts: React.FC<ContactsProps> = ({ contacts }) => {
    const router = useRouter();

    const handleViewDetails = (contactId: string, tenant_unique_name: string) => {
        router.push(`/contacts/${tenant_unique_name}/${contactId}`);
    };

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {contacts.map(contact => (
                <div key={contact.id} className="border p-4 rounded shadow">
                    <h2 className="text-xl font-bold">{contact.title}</h2>
                    <p>{contact.props.Name}</p>
                    <button
                        onClick={() => handleViewDetails(contact.id, contact.tenantUniqueName)}
                        className="btn mt-2 px-4 py-2 bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Show Details
                    </button>
                </div>
            ))}
        </div>
    );
};

export default Contacts;
