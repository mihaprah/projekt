"use client";

import React from 'react';
import {Contact as ContactModel} from '@/models/Contact';
import {Event as EventModel} from '@/models/Event';
import EditContactPopup from './EditContactPopup';
import 'react-toastify/dist/ReactToastify.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser, faPhone, faEnvelope, faTag, faComment} from "@fortawesome/free-solid-svg-icons";
import EventList from "@/Components/Event/EventDisplay";

interface ContactDetailsProps {
    contact: ContactModel;
    activityLog: EventModel[];
    tenantUniqueName: string;
}

const ContactDetails: React.FC<ContactDetailsProps> = ({contact, activityLog, tenantUniqueName}) => {
    if (!contact) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2 className="container mx-auto pr-6 pb-6 pt-6 text-3xl font-semibold">Contact details</h2>
            <div className="container mx-auto p-6 bg-white shadow-xl rounded-8">
                <div className="flex justify-between items-center mb-6">
                    <div className="flex items-center space-x-4">
                        <div>
                            <h1 className="text-2xl font-semibold text-primary">{contact.title}</h1>
                        </div>
                    </div>
                    <EditContactPopup contact={contact} tenantUniqueName={tenantUniqueName}/>
                </div>
                <div className="bg-gray-50 p-4 rounded-8 shadow-sm mb-4">
                    <div className="flex items-center justify-around">
                        <div className="flex flex-col items-center">
                            <div className={"flex items-center"}>
                                <FontAwesomeIcon icon={faUser} className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                <h4 className="font-semibold text-lg text-primary">Full Name</h4>
                            </div>
                            <p className="mt-1 text-gray-700">{contact.props.Name}</p>
                        </div>
                        <div className="flex flex-col items-center">
                            <div className={"flex items-center"}>
                                <FontAwesomeIcon icon={faPhone} className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                <h4 className="font-semibold text-lg text-primary">Contact Number</h4>
                            </div>
                            <p className="mt-1 text-gray-700">{contact.props.Phone}</p>
                        </div>
                        <div className="flex flex-col items-center">
                            <div className={"flex items-center"}>
                                <FontAwesomeIcon icon={faEnvelope} className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                <h4 className="font-semibold text-lg text-primary">Email</h4>
                            </div>
                            <p className="mt-1 text-gray-700">{contact.props.Email}</p>
                        </div>
                    </div>
                </div>
                <hr className="border-gray-300 my-4"/>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    {Object.entries(contact.props).map(([name, value], index) => (
                        name !== "Name" && name !== "Phone" && name !== "Email" && (
                            <div key={index} className="bg-gray-50 p-4 rounded-8 shadow-sm">
                                <h4 className="font-semibold text-lg text-primary">{name}</h4>
                                <p className="mt-1 text-gray-700">{value}</p>
                            </div>
                        )
                    ))}
                </div>
                {contact.comments && (
                    <>
                        <hr className="border-gray-300 my-4"/>
                        <div className="md:col-span-2 bg-gray-50 p-4 rounded-8 shadow-sm">
                            <div className="flex items-center">
                                <FontAwesomeIcon icon={faComment} className={"mr-2 ml-1 w-3.5 h-auto"}/>
                                <h4 className="font-semibold text-lg text-primary">Comments</h4>
                            </div>
                            <p className="mt-1 text-gray-700">{contact.comments}</p>
                        </div>
                    </>
                )}
                <hr className="border-gray-300 my-4"/>
                <div className="bg-gray-50 p-6 rounded-8 shadow-sm">
                    <div className="flex items-center">
                        <FontAwesomeIcon icon={faTag} className="mr-2 mb-4 ml-1 w-3.5 h-auto"/>
                        <h4 className="font-semibold text-lg text-primary mb-4">Tags</h4>
                    </div>
                    <div className="flex flex-wrap gap-2 mb-4">
                        {contact.tags.map((tag, index) => (
                            <span key={index}
                                  className="bg-white text-primary text-primary-light border border-primary-light text-sm font-medium px-3 py-1.5 rounded-8">
                            {tag}</span>
                        ))}
                    </div>
                </div>
                <hr className="border-gray-300 my-4"/>
                <div className="bg-gray-50 p-6 rounded-8 shadow-sm">
                    <h4 className="font-semibold text-lg text-primary mb-4">Activity log</h4>
                    {activityLog.length <= 1 ? (
                        <p className="text-gray-700">No changes were made to this contact yet</p>
                    ) : (
                        <EventList events={activityLog}/>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ContactDetails;
