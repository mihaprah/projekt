import React, {useState} from 'react';
import ContactList from './ContactList';
import {Contact as ContactModel} from '../../models/Contact';

interface Props {
    contacts: ContactModel[];
}

const Contacts: React.FC<Props> = ({contacts}) => {

    return (
        <ContactList contacts={contacts}/>
    );
};

export default Contacts;
