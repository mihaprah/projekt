// src/components/ContactDetails.tsx

"use client";

import React from 'react';
import { Contact as ContactModel } from '@/models/Contact';
import { Event as EventModel } from '@/models/Event';
import EventDisplay from '@/Components/Event/EventDisplay';
import EditContactPopup from './EditContactPopup';

interface ContactDetailsProps {
    contact: ContactModel;
    activityLog: EventModel[];
    tenantUniqueName: string;
}

const ContactDetails: React.FC<ContactDetailsProps> = ({ contact, activityLog, tenantUniqueName }) => {
    if (!contact) {
        return <div>Loading...</div>;
    }

    let date = new Date(contact.createdAt);
    let formattedDate = `${date.getDate()}-${date.getMonth() + 1}-${date.getFullYear()}`;

    return (
        <div className="container mx-auto p-4">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-semibold mt-3">{contact.title}</h1>
                <EditContactPopup contact={contact} />
            </div>
            <div className="flex py-3">
                {contact.tags.map((tag, index) => (
                    <div key={index}
                         className="rounded-8 bg-gray-300 px-4 py-1 mr-2 font-semibold">
                        {tag}
                    </div>
                ))}
            </div>
            <div className="py-3">
                <h4 className="font-semibold">Created by</h4>
                <p className="mt-1">{contact.user}</p>
            </div>
            <div className="py-3">
                <h4 className="font-semibold">Created on</h4>
                <p className="mt-1">{formattedDate}</p>
            </div>
            <div className="flex justify-between">
                <div>
                    <div>
                        {Object.entries(contact.props).map(([name, value], index) => (
                            <div key={index} className="py-3">
                                <h4 className="font-semibold">{name}</h4>
                                <p className="mt-1">{value}</p>
                            </div>
                        ))}
                    </div>
                    {contact.comments && (
                        <div className="py-3 break-words w-96">
                            <h4 className="font-semibold">Comments</h4>
                            <p className="mt-1">{contact.comments}</p>
                        </div>
                    )}
                </div>
                <div className="py-3 min-w-96">
                    <h4 className="font-semibold">Activity log</h4>
                    {activityLog.length <= 1 ? (
                        <p className="mt-1">No changes were made to this contact yet</p>
                    ) : (
                        activityLog.map((event: EventModel, index: number) => (
                            <div key={index}>
                                <EventDisplay event={event} />
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default ContactDetails;
