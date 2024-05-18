import React from 'react';
import Contact from './Contact';
import { Contact as ContactModel } from '../../models/Contact';

interface Props {
    contacts: ContactModel[];
}

const ContactList: React.FC<Props> = ({ contacts }) => (
    <div className="flex flex-col">
        {contacts.length > 0 ? (
            contacts.map(contact => (
                <Contact key={contact.id} contact={contact} />
            ))
        ) : (
            <p>No contacts available.</p>
        )}
    </div>
);

export default ContactList;
