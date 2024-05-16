import { Contact as ContactModel } from '../../models/Contact';

interface Props {
    contact: ContactModel;
}

const Contact: React.FC<Props> = ({ contact }) => (
    <div>
        <h2>{contact.title}</h2>
        <p>User: {contact.user}</p>
        <p>Tenant Unique Name: {contact.tenantUniqueName}</p>
        <p>Comments: {contact.comments}</p>
        <p>Created At: {contact.createdAt.toString()}</p>
        <p>Tags: {contact.tags.join(', ')}</p>
        <p>Props: {JSON.stringify(contact.props)}</p>
        <p>Attributes To String: {contact.attributesToString}</p>
    </div>
);

export default Contact;