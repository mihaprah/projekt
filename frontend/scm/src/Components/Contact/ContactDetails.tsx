"use client";

import React, {useEffect, useState} from 'react';
import {Contact as ContactModel} from '@/models/Contact';
import {Event as EventModel} from '@/models/Event';
import EditContactPopup from './EditContactPopup';
import 'react-toastify/dist/ReactToastify.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUser, faPhone, faEnvelope, faTag, faComment} from "@fortawesome/free-solid-svg-icons";
import EventList from "@/Components/Event/EventDisplay";
import {toast} from "react-toastify";
import {Tenant as TenantModel} from '@/models/Tenant';

interface ContactDetailsProps {
    contact: ContactModel;
    activityLog: EventModel[];
    tenantUniqueName: string;
    IdToken: string;
}

const fetchTenant = async (tenantUniqueName: string, IdToken: string) => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/unique/${tenantUniqueName}`, {
            method: 'GET',
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error deleting contact: ${res.statusText}`);
        }
        return await res.json();
    } catch (error) {
        toast.error('Failed to get tenant');
        console.error('Failed to delete contact:', error);
    }
}

const ContactDetails: React.FC<ContactDetailsProps> = ({contact, activityLog, tenantUniqueName, IdToken}) => {
    const [tenant, setTenant] = useState<TenantModel>();

    useEffect(() => {
        fetchTenant(tenantUniqueName, IdToken).then(tenant => {
            setTenant(tenant);
        });
    }, [tenantUniqueName, IdToken]);

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
                <div className="bg-gray-50 p-4 rounded-8 shadow-sm mb-2">
                    <div className="flex items-center justify-around">
                        {contact.props.name && (
                            <div className="flex flex-col items-center">
                                <div className={"flex items-center"}>
                                    <FontAwesomeIcon icon={faUser} className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                    <h4 className="font-semibold text-lg text-primary">{tenant?.labels["name"]}</h4>
                                </div>
                                <div className={"flex"}>
                                    <p className="mt-1 mr-0.5 text-gray-700">{contact.props.name}</p>
                                    <p className="mt-1 ml-0.5 text-gray-700">{contact.props.lastname}</p>
                                </div>
                            </div>
                        )}
                        {contact.props.phoneNumber && (
                            <div className="flex flex-col items-center">
                                <div className={"flex items-center"}>
                                    <FontAwesomeIcon icon={faPhone} className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                    <h4 className="font-semibold text-lg text-primary">{tenant?.labels["phoneNumber"]}</h4>
                                </div>
                                <p className="mt-1 text-gray-700">{contact.props.phoneNumber}</p>
                            </div>
                        )}
                        {contact.props.email && (
                            <div className="flex flex-col items-center">
                                <div className={"flex items-center"}>
                                    <FontAwesomeIcon icon={faEnvelope} className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                    <h4 className="font-semibold text-lg text-primary">{tenant?.labels["email"]}</h4>
                                </div>
                                <p className="mt-1 text-gray-700">{contact.props.email}</p>
                            </div>
                        )}
                    </div>
                </div>
                {contact.props && (
                    <>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4 mb-4">
                            {Object.entries(contact.props).map(([name, value], index) => (
                                name !== "name" && name !== "phoneNumber" && name !== "email" && (
                                    <div key={index} className="bg-gray-50 p-4 rounded-8 shadow-sm">
                                        <h4 className="font-semibold text-lg text-primary">{tenant?.labels[name]}</h4>
                                        <p className="mt-1 text-gray-700">{value}</p>
                                    </div>
                                )
                            ))}
                        </div>
                    </>
                )}
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
                {contact.tags && contact.tags.length > 0 && (
                    <>
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
                    </>
                )}
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
