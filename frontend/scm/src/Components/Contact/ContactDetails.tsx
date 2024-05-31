"use client";

import React, {useEffect, useState} from 'react';
import {Contact as ContactModel} from '@/models/Contact';
import {Event as EventModel} from '@/models/Event';
import EditContactPopup from './EditContactPopup';
import 'react-toastify/dist/ReactToastify.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faUser,
    faPhone,
    faEnvelope,
    faTag,
    faComment,
    faArrowLeft,
    faBuilding, faAddressBook, faHouse, faAddressCard
} from "@fortawesome/free-solid-svg-icons";
import EventList from "@/Components/Event/EventDisplay";
import {toast} from "react-toastify";
import {useRouter} from 'next/navigation';
import {Tenant as TenantModel} from '@/models/Tenant';
import Link from "next/link";
import Loading from "@/app/loading";

interface ContactDetailsProps {
    contact: ContactModel;
    activityLog: EventModel[];
    tenantUniqueName: string;
    IdToken: string;
    tenantTitle: string;
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
            toast.error(res.statusText || 'Failed to get tenant');
        }
        return await res.json();
    } catch (error: any) {
        toast.error(error.message || 'Failed to get tenant');
    }
}

const ContactDetails: React.FC<ContactDetailsProps> = ({contact, activityLog, tenantUniqueName, IdToken, tenantTitle}) => {
    const router = useRouter();
    const [tenant, setTenant] = useState<TenantModel>();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchTenant(tenantUniqueName, IdToken).then(tenant => {
            if (!tenant) {
                router.push('/404');
            } else {
                setTenant(tenant);
                setLoading(false);
            }
        });
    }, [tenantUniqueName, IdToken, router]);

    if (!contact || !contact.props) {
        router.push('/404');
        return null;
    }

    return (
        <div className={"container mx-auto p-6"}>
            <div className={"container mx-auto flex items-center"}>
                <FontAwesomeIcon
                    icon={faArrowLeft}
                    className="text-primary-light mr-4 cursor-pointer w-3.5 h-auto"
                    onClick={() => router.back()}
                />
                <div className="text-sm breadcrumbs mx-2">
                    <ul className={"text-gray-500"}>
                        <li><Link
                            href={"/"}><FontAwesomeIcon icon={faHouse} className={"mr-1"}/>Home</Link></li>
                        <li><Link
                            href={`/contacts/${tenantUniqueName}`}><FontAwesomeIcon icon={faAddressBook} className={"mr-1"}/>{tenantTitle}</Link></li>
                        <li><Link
                            href={"#"}><FontAwesomeIcon icon={faAddressCard} className={"mr-1"}/>{contact.title}</Link></li>
                    </ul>
                </div>
            </div>
            <div className="container mx-auto pr-6 pb-6 pt-2 flex items-center">
                <h2 className="text-3xl font-semibold">Contact details</h2>
            </div>
            {loading ? (
                <Loading />
            ) : (
                <>
                    <div className="container mx-auto p-6 bg-white shadow-xl rounded-8">
                        <div className="flex justify-between items-center mb-6">
                            <div className="flex items-center space-x-4">
                                <div className={"flex"}>
                                    <FontAwesomeIcon icon={faUser}
                                                     className="text-primary mr-3 w-5 mb-1.5 h-auto"/>
                                    <span className="text-2xl font-semibold text-primary">
                                        {contact.props.prefix && (
                                            <span className={"mr-1"}>{contact.props.prefix}</span>
                                        )}
                                        {contact.title}
                                    </span>
                                </div>
                            </div>
                            <EditContactPopup contact={contact} tenantUniqueName={tenantUniqueName}/>
                        </div>
                        {(contact.props.fullName || contact.props.phoneNumber || contact.props.email) && (
                            <div className="bg-gray-50 p-4 rounded-8 shadow-sm mb-2">
                                <div className="flex items-center justify-around">
                                    {contact.props.phoneNumber && (
                                        <div className="flex flex-col items-center">
                                            <div className={"flex items-center"}>
                                                <FontAwesomeIcon icon={faPhone}
                                                                 className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                                <h4 className="font-semibold text-lg text-primary">{tenant?.labels["phoneNumber"]}</h4>
                                            </div>
                                            <p className="mt-1 text-gray-700">{contact.props.phoneNumber}</p>
                                        </div>
                                    )}
                                    {contact.props.email && (
                                        <div className="flex flex-col items-center">
                                            <div className={"flex items-center"}>
                                                <FontAwesomeIcon icon={faEnvelope}
                                                                 className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                                <h4 className="font-semibold text-lg text-primary">{tenant?.labels["email"]}</h4>
                                            </div>
                                            <a href={`mailto:${contact.props.email}`}
                                               onClick={(e) => e.stopPropagation()}
                                               className="mt-1 text-primary-light hover:underline">
                                                {contact.props.email}
                                            </a>
                                        </div>
                                    )}
                                    {contact.props.company && (
                                        <div className="flex flex-col items-center">
                                            <div className={"flex items-center"}>
                                                <FontAwesomeIcon icon={faBuilding}
                                                                 className="text-primary mr-3 ml-1 w-3.5 h-auto"/>
                                                <h4 className="font-semibold text-lg text-primary">{tenant?.labels["company"]}</h4>
                                            </div>
                                            <div className={"flex"}>
                                                <p className="mt-1 mr-0.5 text-gray-700">{contact.props.company}</p>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {contact.props && (
                            <>
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4 mb-4">
                                    {Object.entries(contact.props).map(([name, value], index) => (
                                        name !== "company" && name !== "phoneNumber" && name !== "email" && name !== "prefix" && (
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
                            {activityLog.length <= 0 ? (
                                <p className="text-gray-700">No changes were made to this contact yet</p>
                            ) : (
                                <EventList events={activityLog}/>
                            )}
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default ContactDetails;
