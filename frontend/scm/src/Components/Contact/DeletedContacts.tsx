"use client";
import { Contact as ContactModel } from '../../models/Contact';
import { Tenant as TenantModel } from '../../models/Tenant';
import React, {useEffect} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faCircleInfo} from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";
import {useRouter} from "next/navigation";
import Contacts from "@/Components/Contact/Contacts";
import Loading from "@/app/loading";
import {toast} from "react-toastify";
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
            toast.error(res.statusText || 'Failed to fetch deleted contacts');
            return [];
        }

        const contacts = await res.json();

        if (!Array.isArray(contacts)) {
            toast.error('Fetched data is not an array');
            return [];
        }

        return contacts;
    } catch (error: any) {
        toast.error(error.message || 'Failed to fetch deleted contacts')
        return [];
    }
};
const DeletedContacts: React.FC<DeletedContactsProps> = (props) => {
    const router = useRouter();
    const [contacts, setContacts] = React.useState<ContactModel[]>([]);
    const [loading, setLoading] = React.useState<boolean>(true);

    useEffect(() => {
        fetchDeletedContacts(props.tenant.tenantUniqueName, props.IdToken).then(contacts => {
            setContacts(contacts);
            setLoading(false);
        });
    },[props.tenant.tenantUniqueName, props.IdToken]);

    const handleContactChange = async () => {
        const fetchContacts = await fetchDeletedContacts(props.tenant.tenantUniqueName, props.IdToken);
        setContacts(fetchContacts);
    }

    return (
        <>
            {loading ? (
                <Loading />
            ) : (
                <>
                    <div className={"flex items-center"}>
                        <FontAwesomeIcon
                            icon={faArrowLeft}
                            className="text-primary-light mr-4 cursor-pointer w-3.5 h-auto"
                            onClick={() => router.back()}
                        />
                        <div className="text-sm breadcrumbs mx-2">
                            <ul className={"text-gray-500"}>
                                <li><Link
                                    href={"/"}>Home</Link></li>
                                <li><Link
                                    href={`/contacts/${props.tenant.tenantUniqueName}`}>{props.tenant.title}</Link></li>
                                <li><Link
                                    href={"#"}>Deleted contacts</Link></li>
                            </ul>
                        </div>
                    </div>
                    <div className={"flex items-center mt-3"}>
                        <h2 className="text-3xl font-semibold text-primary-light">Deleted contacts
                            - {props.tenant.title}</h2>
                    </div>
                    <div className={"flex mt-0 mb-3 items-center"}>
                        <p className={"font-light text-sm"}>Deleted Contacts list for group {props.tenant.title} </p>
                        <div className="tooltip tooltip-right"
                             data-tip="When you delete a Contact it is moved to Deleted contacts page, where you can see which contacts have been deleted for each group. You can also permanently delete a Contact from the database, this will remove all data assosiated wiht the Contact."  >
                            <FontAwesomeIcon className="ml-1 w-3.5 h-auto" style={{color: "#007BFF"}}
                                             icon={faCircleInfo}/>
                        </div>
                    </div>
                    {contacts.length !== 0 ? (
                        <Contacts contacts={contacts} tenantUniqueName={props.tenant.tenantUniqueName}
                                  tenantId={props.tenant.id}
                                  IdToken={props.IdToken} view={'list'} onChange={handleContactChange}
                                  tenant={props.tenant} deleted={true}/>
                    ) : (
                        <div className="flex flex-col mt-20">
                            <div className="flex items-center justify-center">
                                <p className="text-xl mb-24">No deleted contacts yet!</p>
                            </div>
                        </div>
                    )}
                </>
            )}
        </>
    )
}

export default DeletedContacts;