import React from 'react';
import { cookies } from 'next/headers';
import { Contact as ContactModel } from '../../../models/Contact';
import { Tenant as TenantModel } from '../../../models/Tenant';
import Contacts from "@/Components/Contact/Contacts";

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
            <Contacts
                contacts={contacts}
                tenant={tenant}
                contactsNumber={contactsNumber}
                tenantUniqueName={tenant_unique_name}
                IdToken={IdToken}
            />
        </div>
    );
};

export default ContactsPage;
