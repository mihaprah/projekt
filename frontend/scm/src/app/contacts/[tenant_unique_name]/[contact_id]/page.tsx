// src/app/contacts/[tenant_unique_name]/[contact_id]/page.tsx
import React from 'react';
import { cookies } from 'next/headers';
import { Contact as ContactModel } from '@/models/Contact';
import ContactEditForm from "@/Components/Contact/ContactEditForm";

interface ContactPageProps {
    tenant_unique_name: string;
    contactId: string;
}

const fetchContact = async (contactId: string, IdToken: string, tenant_unique_name: string): Promise<ContactModel> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${contactId}/${tenant_unique_name}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching contact: ${res.statusText}`);
        }

        const contact = await res.json();
        return contact;
    } catch (error) {
        console.error('Failed to fetch contact:', error);
        return {} as ContactModel;
    }
};

const ContactPage = async ({ params }: { params: { tenant_unique_name: string, contact_id: string } }) => {
    const { tenant_unique_name, contact_id } = params;
    const IdToken = cookies().get('IdToken')?.value || '';
    const contact = await fetchContact(contact_id, IdToken, tenant_unique_name);

    if (!contact) {
        return <div>Loading...</div>;
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-3xl font-bold">{contact.title}</h1>
            <p>{contact.comments}</p>
            <ContactEditForm contact={contact} />
        </div>
    );
};

export default ContactPage;
