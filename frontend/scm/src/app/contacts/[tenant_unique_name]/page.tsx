import React from 'react';
import { cookies } from 'next/headers';
import { Contact as ContactModel } from '../../../models/Contact';
import { Tenant as TenantModel } from '../../../models/Tenant';
import Contacts from "@/Components/Contact/Contacts";
import {faFileArrowDown, faGear, faPen, faPlus, faUsers} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const fetchContacts = async (tenant_unique_name: string, IdToken: string): Promise<ContactModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${tenant_unique_name}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching contacts: ${res.statusText}`);
        }

        const contacts = await res.json();

        if (!Array.isArray(contacts)) {
            throw new Error('Fetched data is not an array');
        }

        return contacts;
    } catch (error) {
        console.error('Failed to fetch contacts:', error);
        return [];
    }
};

const fetchTenant = async (tenant_unique_name: string, IdToken: string): Promise<TenantModel> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/unique/${tenant_unique_name}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching tenant: ${res.statusText}`);
        }

        const tenant = await res.json();
        console.log(tenant)

        return tenant;
    } catch (error) {
        console.error('Failed to fetch tenant:', error);
        return {} as TenantModel;
    }
};

const fetchNumberContacts = async (IdToken: string, tenantUniqueName: string): Promise<number> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/size/${tenantUniqueName}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching contacts number from a tenant: ${res.statusText}`);
        }

        return await res.json();
    } catch (error) {
        console.error('Failed to fetch number of contacts:', error);
        return 0;
    }
}

const ContactsPage = async (props: { params: { tenant_unique_name: string } }) => {
    const { params } = props;
    const { tenant_unique_name } = params;
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        return <p>IdToken is not available</p>;
    }

    const contacts = await fetchContacts(tenant_unique_name, IdToken);
    const tenant = await fetchTenant(tenant_unique_name, IdToken);
    const contactsNumber = await fetchNumberContacts(IdToken, tenant_unique_name);

    return (
        <div className="container mx-auto p-4">
            <div className="flex justify-between mb-4 ">
                <div>
                    <div className={"flex items-center"}>
                        <h2 className="text-3xl font-semibold text-primary-light">{tenant.title}</h2>
                        <span className="ml-6 mr-2 text-2xl font-semibold ">{contactsNumber}</span>
                        <FontAwesomeIcon className="h-5 w-auto mb-0.5 " icon={faUsers}/>
                    </div>
                    <p className="text-m text-gray-600 mt-1 break-words max-w-96">{tenant.description}</p>
                    <div className={"mt-4"}>
                        <button type="button"
                                className="btn mr-3 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                            Edit Tenant
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faPen} />
                        </button>
                        <button type="button"
                                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                            Tenant Settings
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faGear} />
                        </button>
                    </div>
                </div>
                <div className="flex items-end">
                    <button
                        className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Add new Contact
                        <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faPlus}/>
                    </button>
                    <button
                        className="btn ml-3 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Export excel
                        <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faFileArrowDown} />
                    </button>
                </div>
            </div>
            {contactsNumber === 0 && (
                <p className="text-center text-2xl mx-auto mt-10">No contacts added to this tenant yet.</p>
            )}
            <Contacts contacts={contacts}/>
        </div>
    );
};

export default ContactsPage;
