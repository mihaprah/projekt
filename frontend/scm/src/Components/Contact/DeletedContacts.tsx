"use client";
import { Contact as ContactModel } from '../../models/Contact';
import { Tenant as TenantModel } from '../../models/Tenant';
import React, {useEffect} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faPlus, faTrash, faUsers} from "@fortawesome/free-solid-svg-icons";
import {ToastContainer} from "react-toastify";
import Link from "next/link";
import {useRouter} from "next/navigation";
import Contacts from "@/Components/Contact/Contacts";
interface DeletedContactsProps {
    IdToken: string;
    tenant: TenantModel;
}

const fetchDeletedContacts = async (tenant_unique_name: string, IdToken: string): Promise<ContactModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${tenant_unique_name}/deleted`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching deleted contacts: ${res.statusText}`);
        }

        const contacts = await res.json();

        if (!Array.isArray(contacts)) {
            throw new Error('Fetched data is not an array');
        }

        return contacts;
    } catch (error) {
        console.error('Failed to fetch deleted contacts:', error);
        return [];
    }
};
const DeletedContacts: React.FC<DeletedContactsProps> = (props) => {
    const router = useRouter();
    const [contacts, setContacts] = React.useState<ContactModel[]>([]);

    useEffect(() => {
        fetchDeletedContacts(props.tenant.tenantUniqueName, props.IdToken).then(contacts => {
            setContacts(contacts);
        });
    },[props.tenant.tenantUniqueName]);

    const handleContactChange = async () => {
        const fetchContacts = await fetchDeletedContacts(props.tenant.tenantUniqueName, props.IdToken);
        setContacts(fetchContacts);
    }

    return (
        <>
            <ToastContainer/>
            <div className={"flex items-center"}>
                <FontAwesomeIcon
                    icon={faArrowLeft}
                    className="text-primary-light mr-4 cursor-pointer w-3.5 h-auto"
                    onClick={() => router.back()}
                />
                <div className="text-sm breadcrumbs mx-2">
                    <ul className={"text-gray-500"}>
                        <li><Link
                            href={"/"}>Tenants</Link></li>
                        <li><Link
                            href={`/contacts/${props.tenant.tenantUniqueName}`}>{props.tenant.title}</Link></li>
                        <li><Link
                            href={"#"}>Deleted contacts</Link></li>
                    </ul>
                </div>
            </div>
            <div className={"flex items-center mt-3"}>
                <h2 className="text-3xl font-semibold text-primary-light mb-5">Deleted contacts - {props.tenant.title}</h2>
            </div>
            {contacts.length !== 0 ? (
                <Contacts contacts={contacts} tenantUniqueName={props.tenant.tenantUniqueName} tenantId={props.tenant.id}
                          IdToken={props.IdToken} view={'list'} onDeleted={handleContactChange} tenant={props.tenant} deleted={true} />
            ) : (
                <div className="flex flex-col mt-20">
                    <div className="flex items-center justify-center">
                        <p className="text-xl mb-24">No deleted contacts yet!</p>
                    </div>
                </div>
            )}

        </>
    )
}

export default DeletedContacts;