import React from 'react';
import { Contact as ContactModel } from '../../models/Contact';

interface ContactProps {
    contact: ContactModel;
}

const Contact: React.FC<ContactProps> = ({ contact }) => (
    <div className="card bg-base-100 shadow-xl m-2">
        <div className="card-body">
            <h2 className="card-title">{contact.title}</h2>
            <p><strong>Name:</strong> {contact.props.Name}</p>
            <p><strong>Created At:</strong> {new Date(contact.createdAt).toLocaleString()}</p>
        </div>
    </div>
);

export default Contact;
