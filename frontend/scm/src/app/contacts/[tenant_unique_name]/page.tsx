import React from 'react';
import { cookies } from 'next/headers';
import { Contact as ContactModel } from '../../../models/Contact';
import { Tenant as TenantModel } from '../../../models/Tenant';
import Contacts from "@/Components/Contact/Contacts";
import {faUsers} from "@fortawesome/free-solid-svg-icons";
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

const ContactsPage = async ({ params, searchParams }: { params: { tenant_unique_name: string }, searchParams: { view?: string } }) => {
    const { tenant_unique_name } = params;
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        return <p>IdToken is not available</p>;
    }

    const contacts = await fetchContacts(tenant_unique_name, IdToken);
    const tenant = await fetchTenant(tenant_unique_name, IdToken);
    const isGridView = searchParams.view === 'grid';

    return (
        <div className="container mx-auto p-4">
            <div className="flex justify-between items-center mb-4">
                <div>
                    <h1 className="text-3xl font-bold text-primary-light">{tenant.title} <span
                        className="ml-5 mr-2 text-secondary-dark">{contacts.length}</span><FontAwesomeIcon
                        className="h-6 w-auto mb-0.5 text-accent-dark" icon={faUsers}/>
                    </h1>
                    <p className="text-m text-gray-600 mt-1">{tenant.description}</p>
                    <button type="button"
                            className="btn mt-2 px-6 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Edit Tenant
                    </button>
                </div>
                <div className="flex space-x-2">
                    <button
                        className="btn mt-2 px-6 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Add new contact
                    </button>
                    <button
                        className="btn mt-2 px-6 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                        Export excel
                    </button>
                </div>
            </div>
            <Contacts contacts={contacts}/>
        </div>
    );
};

export default ContactsPage;
