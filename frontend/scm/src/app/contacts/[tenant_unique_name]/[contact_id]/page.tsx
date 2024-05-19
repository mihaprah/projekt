// src/app/contacts/[tenant_unique_name]/[contact_id]/page.tsx
import React from 'react';
import {cookies} from 'next/headers';
import {Contact as ContactModel} from '@/models/Contact';
import {Event as EventModel} from '@/models/Event';
import EventDisplay from "@/Components/Event/EventDisplay";

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

        return await res.json();
    } catch (error) {
        console.error('Failed to fetch contact:', error);
        return {} as ContactModel;
    }
};

const fetchActivityLog = async (contactId: string, IdToken: string, tenant_unique_name: string) => {
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

    if (!contact) {
        return <div>Loading...</div>;
    }
    let date = new Date(contact.createdAt);
    let formattedDate = `${date.getDate()}-${date.getMonth()+1}-${date.getFullYear()}`;

    return (
        <div className="container mx-auto p-4">
            <div className={"flex justify-between items-center"}>
                <h1 className="text-3xl font-semibold mt-3">{contact.title}</h1>
                <button className={"bg-primary-light text-white rounded-8 px-4 py-1 hover:scale-105 transition"}>Edit Contact</button>
            </div>
            <div className="flex py-3">
                {contact.tags.map((tag, index) => (
                    <div key={index}
                         className={"rounded-8 bg-gray-300 px-4 py-1 mr-2 font-semibold hover:scale-105 transition"}>{tag}</div>
                ))}
            </div>
            <div className="py-3">
                <h4 className={"font-semibold"}>Created by</h4>
                <p className={"mt-1"}>{contact.user}</p>
            </div>
            <div className="py-3">
                <h4 className={"font-semibold"}>Created on</h4>
                <p className={"mt-1"}>{formattedDate}</p>
            </div>
            <div className={"flex justify-between"}>
                <div>
                    <div>
                        {Object.entries(contact.props).map(([name, value], index) => (
                            <div key={index} className={"py-3"}>
                                <h4 className={"font-semibold"}>{name}</h4>
                                <p className={"mt-1"}>{value}</p>
                            </div>
                        ))}
                    </div>
                    {contact.comments && (
                        <div className="py-3 break-words w-96">
                            <h4 className={"font-semibold"}>Comments</h4>
                            <p className={"mt-1"}>{contact.comments}</p>
                        </div>

                    )}
                </div>
                <div className={"py-3 min-w-96"}>
                    <h4 className={"font-semibold"}>Activity log</h4>
                    {activityLog.length <= 1 ? (
                        <p className={"mt-1"}>No changes were made to this contact yet</p>
                    ) : (
                        activityLog.map((event: EventModel, index: number) => (
                            <div key={index}>
                                <EventDisplay event={event}/>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default ContactPage;
