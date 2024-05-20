// src/app/contacts/[tenant_unique_name]/[contact_id]/page.tsx

import React from 'react';
import { cookies } from 'next/headers';
import { Contact as ContactModel } from '@/models/Contact';
import { Event as EventModel } from '@/models/Event';
import ContactDetails from '@/Components/Contact/ContactDetails';

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

        return await res.json();
    } catch (error) {
        console.error('Failed to fetch contact:', error);
        return {} as ContactModel;
    }
};

const fetchActivityLog = async (contactId: string, IdToken: string, tenant_unique_name: string): Promise<EventModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/events/${contactId}/${tenant_unique_name}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching activity log: ${res.statusText}`);
        }

        return await res.json();
    } catch (error) {
        console.error('Failed to fetch activity log:', error);
        return [] as EventModel[];
    }
}

const ContactPage = async ({ params }: { params: { tenant_unique_name: string, contact_id: string } }) => {
    const { tenant_unique_name, contact_id } = params;
    const IdToken = cookies().get('IdToken')?.value || '';
    const contact = await fetchContact(contact_id, IdToken, tenant_unique_name);
    const activityLog: EventModel[] = await fetchActivityLog(contact_id, IdToken, tenant_unique_name);

    return (
        <ContactDetails contact={contact} activityLog={activityLog} tenantUniqueName={tenant_unique_name} />
    );
};

export default ContactPage;