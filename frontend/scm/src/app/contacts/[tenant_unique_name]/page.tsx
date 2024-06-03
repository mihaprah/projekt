
import React from 'react';
import {cookies} from 'next/headers';
import {Contact as ContactModel} from '../../../models/Contact';
import {Tenant as TenantModel} from '../../../models/Tenant';
import SearchContacts from "@/Components/Search/SearchContacts";
import 'react-toastify/dist/ReactToastify.css';
import {redirect} from "next/navigation";

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

        return await res.json();
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

const fetchNumberOfTenantsOnUser = async (IdToken: string): Promise<TenantModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/user`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching number of tenants on user: ${res.statusText}`);
        }

        return await res.json();
    } catch (error) {
        console.error('Failed to fetch number of tenants on user:', error);
        return [];
    }

}

const ContactsPage = async (props: { params: { tenant_unique_name: string, search_id: string } }) => {
    const { params } = props;
    const { tenant_unique_name, search_id } = params;
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        redirect('/login');
    }

    const contacts = await fetchContacts(tenant_unique_name, IdToken);
    const tenant = await fetchTenant(tenant_unique_name, IdToken);
    const contactsNumber = await fetchNumberContacts(IdToken, tenant_unique_name);
    const tenants = await fetchNumberOfTenantsOnUser(IdToken);

    return (
        <>
            <head>
                <title>SCM - Contacts</title>
            </head>
            <div className="container mx-auto p-4">
                <SearchContacts contacts={contacts} tenant={tenant} contactsNumber={contactsNumber}
                                tenantUniqueName={tenant_unique_name} IdToken={IdToken} numberOfTenants={tenants.length}
                                searchId={search_id}/>
            </div>
        </>
    );
};

export default ContactsPage;
