import {cookies} from "next/headers";
import SavedSearchesTable from "@/Components/SavedSearches/SavedSearchesTable";
import React from "react";
import {redirect} from "next/navigation";


const SavedSearchesPage: React.FC = async () => {
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        redirect('/login');
    }


    return (
            <SavedSearchesTable IdToken={IdToken}></SavedSearchesTable>
    );
}

export default SavedSearchesPage;