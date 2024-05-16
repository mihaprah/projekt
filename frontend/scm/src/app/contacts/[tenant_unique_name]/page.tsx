import { cookies } from 'next/headers';
import Contact from '../../../Components/Contact/Contact';
import { Contact as ContactModel } from '../../../models/Contact';

interface ContactsPageProps {
    params: {
        tenant_unique_name: string;
    };
}

const fetchContacts = async (tenant_unique_name: string, IdToken: string): Promise<ContactModel[]> => {
    try {
        const res = await fetch(`http://localhost:8080/contacts/${tenant_unique_name}`, {
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

const ContactsPage: React.FC<ContactsPageProps> = async ({ params }) => {
    const tenant_unique_name = params.tenant_unique_name;
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        throw new Error('IdToken is not available');
    }

    const contacts = await fetchContacts(tenant_unique_name, IdToken);

    return (
        <div>
            {contacts.length === 0 ? (
                <p>No contacts available</p>
            ) : (
                contacts.map((contact) => (
                    <Contact key={contact.id} contact={contact} />
                ))
            )}
        </div>
    );
};

export default ContactsPage;
