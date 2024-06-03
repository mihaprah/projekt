import ContactsPage from "@/app/contacts/[tenant_unique_name]/page";
import 'react-toastify/dist/ReactToastify.css';
import React from "react";
const SearchPage = (props: { params: { tenant_unique_name: string, search_id: string } }) => {
    const { params } = props;
    const { tenant_unique_name, search_id } = params;

    return (
        <>
            <head>
                <title>SCM - Contacts</title>
            </head>
            <ContactsPage params={{tenant_unique_name, search_id}}/>
        </>

);
}

export default SearchPage;