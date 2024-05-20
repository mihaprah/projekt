"use client";

import React from 'react';
import {Contact as ContactModel} from '@/models/Contact';
import {Event as EventModel} from '@/models/Event';
import EventDisplay from '@/Components/Event/EventDisplay';
import EditContactPopup from './EditContactPopup';
import 'react-toastify/dist/ReactToastify.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser, faPhone, faEnvelope, faTag, faComment} from "@fortawesome/free-solid-svg-icons";
interface ContactDetailsProps {
    contact: ContactModel;
    activityLog: EventModel[];
    tenantUniqueName: string;
}

const ContactDetails: React.FC<ContactDetailsProps> = ({contact, activityLog, tenantUniqueName}) => {
    if (!contact) {
        return <div>Loading...</div>;
    }

    let date = contact.createdAt ? new Date(contact.createdAt) : new Date();
    let formattedDate = `${date.getDate()}-${date.getMonth() + 1}-${date.getFullYear()}`;

    return (
        <div>
            <h2 className="container mx-auto pr-6 pb-6 pt-6 text-3xl font-semibold">Contact details</h2>
            <div className="container mx-auto p-6 bg-white shadow-xl rounded-md">
                <div className="flex justify-between items-center mb-6">
                    <div className="flex items-center space-x-4">
                        <div>
                            <h1 className="text-2xl font-semibold text-primary">{contact.title}</h1>
                        </div>
                    </div>
                    <EditContactPopup contact={contact} tenantUniqueName={tenantUniqueName}/>
                </div>
                <div className="bg-gray-50 p-4 rounded shadow-sm mb-4">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div className="flex items-center space-x-2">
                            <FontAwesomeIcon icon={faUser} className="text-primary mr-3"/>
                            <div>
                                <h4 className="font-semibold text-lg text-primary">Full Name</h4>
                                <p className="mt-1 text-gray-700">{contact.props.Name}</p>
                            </div>
                        </div>
                        <div className="flex items-center space-x-2">
                            <FontAwesomeIcon icon={faPhone} className="text-primary mr-3"/>
                            <div>
                                <h4 className="font-semibold text-lg text-primary">Contact Number</h4>
                                <p className="mt-1 text-gray-700">{contact.props.Phone}</p>
                            </div>
                        </div>
                        <div className="flex items-center space-x-2">
                            <FontAwesomeIcon icon={faEnvelope} className="text-primary mr-3"/>
                            <div>
                                <h4 className="font-semibold text-lg text-primary">Email</h4>
                                <p className="mt-1 text-gray-700">{contact.props.Email}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <hr className="border-gray-300 my-4"/>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
                    {Object.entries(contact.props).map(([name, value], index) => (
                        name !== "Name" && name !== "Phone" && name !== "Email" && (
                            <div key={index} className="bg-gray-50 p-4 rounded shadow-sm">
                                <h4 className="font-semibold text-lg text-primary">{name}</h4>
                                <p className="mt-1 text-gray-700">{value}</p>
                            </div>
                        )
                    ))}
                    {contact.comments && (
                        <div className="md:col-span-2 bg-gray-50 p-4 rounded shadow-sm">
                            <div className="flex items-center">
                                <FontAwesomeIcon icon={faComment} className={"mr-2"} />
                            <h4 className="font-semibold text-lg text-primary">Comments</h4>
                            </div>
                            <p className="mt-1 text-gray-700">{contact.comments}</p>
                        </div>
                    )}
                </div>
                <hr className="border-gray-300 my-4"/>
                <div className="bg-gray-100 p-6 rounded-md shadow-sm">
                    <div className="flex items-center">
                    <FontAwesomeIcon icon={faTag} className="mr-2 mb-4" />
                    <h4 className="font-semibold text-lg text-primary mb-4">Tags</h4>
                    </div>
                    <div className="flex flex-wrap gap-2 mb-4">
                        {contact.tags.map((tag, index) => (
                            <span key={index}
                                  className="bg-white text-primary text-primary-light border border-primary-light text-sm font-medium px-3 py-1.5 rounded">
                            {tag}</span>
                        ))}
                    </div>
                </div>
                <hr className="border-gray-300 my-4"/>
                <div className="bg-gray-100 p-6 rounded-md shadow-sm">
                    <h4 className="font-semibold text-lg text-primary mb-4">Activity log</h4>
                    {activityLog.length <= 1 ? (
                        <p className="text-gray-700">No changes were made to this contact yet</p>
                    ) : (
                        activityLog.map((event: EventModel, index: number) => (
                            <EventDisplay key={index} event={event}/>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default ContactDetails;
