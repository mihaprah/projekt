import {cookies} from "next/headers";
import SavedSearchesTable from "@/Components/SavedSearches/SavedSearchesTable";
import React from "react";


const SavedSearchesPage: React.FC = async () => {
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        throw new Error('IdToken is not available');
    }


    return (
            <SavedSearchesTable IdToken={IdToken}></SavedSearchesTable>
    );
}

export default SavedSearchesPage;