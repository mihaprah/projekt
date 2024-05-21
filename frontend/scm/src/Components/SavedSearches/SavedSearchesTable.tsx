"use client";
import {PredefinedSearch as SavedSearchesModel} from "@/models/PredefinedSearch";
import React, {useEffect, useState} from "react";
import {faPen, faTrash, faArrowUp, faArrowDown} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SavedSearchesPopup from "@/Components/SavedSearches/SavedSearchesPopup";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

interface SavedSearchesTableProps {
    IdToken: string;
}

const fetchSearches = async (IdToken: string): Promise<SavedSearchesModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches/user`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching predefined searches: ${res.statusText}`);
        }

        const savedSearches = await res.json();

        if (!Array.isArray(savedSearches)) {
            throw new Error('Fetched data is not an array');
        }

        return savedSearches;
    } catch (error) {
        console.error('Failed to fetch predefined searches:', error);
        return [];
    }
}

const SavedSearchesTable: React.FC<SavedSearchesTableProps> = (props) => {
    const [savedSearches, setSavedSearches] = useState<SavedSearchesModel[]>([]);

    useEffect(()=> {
        const fetch = async () => {
            const fetchedSavedSearches = await fetchSearches(props.IdToken);
            setSavedSearches(fetchedSavedSearches);
        };
        fetch();
    }, [props.IdToken]);

    const handleSavedSearchAction = async (IdToken: string) => {
        setSavedSearches(await fetchSearches(IdToken));
    }

    return (
        <div className="container mx-auto p-4">
            <ToastContainer />
            <h1 className="text-3xl pt-5 text-secondary-dark font-semibold mb-5">Saved searches</h1>
        <div className="overflow-x-auto">
            {savedSearches.length === 0 ? (<p className="text-center text-2xl mx-auto mt-10">No saved searches found!</p>
            ) : (
                <table className="table border">
                    <thead className="text-secondary-dark">
                    <tr>
                        <th></th>
                        <th>Title</th>
                        <th>Search query</th>
                        <th>On Tenant</th>
                        <th>Orientation</th>
                        <th>Filter</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    </thead>
                    <tbody>

                    {savedSearches.map((search, index) => (
                            <tr key={search.id}>
                            <td>{index + 1}</td>
                            <td>{search.title}</td>
                            <td>{search.searchQuery}</td>
                            <td>{search.onTenant}</td>
                                <td>
                                    {search.sortOrientation}
                                    {search.sortOrientation === 'ASC' ?
                                        <FontAwesomeIcon className="ml-1 w-2.5 h-auto" icon={faArrowUp}/> :
                                        <FontAwesomeIcon className="ml-1 w-2.5 h-auto" icon={faArrowDown}/>
                                    }
                                </td>
                                <td>
                                    {search.filter.length > 4
                                        ? `${search.filter.slice(0, 4).join(', ')} + more`
                                        : search.filter.join(', ')
                                    }
                                </td>
                                <td>
                                    <SavedSearchesPopup icon={faPen} title={"Edit Search"} savedSearch={search} IdToken={props.IdToken} onSavedSearchAction={() => handleSavedSearchAction(props.IdToken)} action={"edit"} />
                                </td>
                            <td>
                                <SavedSearchesPopup icon={faTrash} title={"Delete Search"} savedSearch={search} IdToken={props.IdToken} onSavedSearchAction={() => handleSavedSearchAction(props.IdToken)} action={"delete"}/>
                            </td>
                        </tr>

                    ))}
                    </tbody>
                </table>
            )}
        </div>
        </div>
    );
}

export default SavedSearchesTable;