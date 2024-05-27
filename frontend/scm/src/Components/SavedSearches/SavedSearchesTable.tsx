"use client";
import {PredefinedSearch as SavedSearchesModel} from "@/models/PredefinedSearch";
import React, {useEffect, useState} from "react";
import {faPen, faTrash, faArrowUp, faArrowDown, faCircleInfo} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SavedSearchesPopup from "@/Components/SavedSearches/SavedSearchesPopup";
import 'react-toastify/dist/ReactToastify.css';
import {useRouter} from "next/navigation";
import {PredefinedSearch as SearchModel} from "@/models/PredefinedSearch";
import Loading from "@/app/loading";
import {toast} from "react-toastify";

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
            toast.error(res.statusText || 'Failed to fetch predefined searches');
            return [];
        }

        const savedSearches = await res.json();

        if (!Array.isArray(savedSearches)) {
            toast.error('Fetched data is not an array');
            return [];
        }

        return savedSearches;
    } catch (error: any) {
        toast.error(error.message || 'Failed to fetch predefined searches');
        return [];
    }
}

const SavedSearchesTable: React.FC<SavedSearchesTableProps> = (props) => {
    const [savedSearches, setSavedSearches] = useState<SavedSearchesModel[]>([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(()=> {
        const fetch = async () => {
            return await fetchSearches(props.IdToken);
        };
        fetch().then((fetchedSavedSearches) => {
            setSavedSearches(fetchedSavedSearches);
            setLoading(false);
        });
    }, [props.IdToken]);

    const handleSearchClick = (search: SearchModel) => {
        router.push(`/contacts/${search.onTenant}/search/${search.id}`)
    }

    const handleSavedSearchAction = async (IdToken: string) => {
        setSavedSearches(await fetchSearches(IdToken));
    }

    return (
        <div>
            {loading ? (
            <Loading />
        ) : (
                <div className="container mx-auto p-4">
                    <h1 className="text-3xl pt-5 text-secondary-dark font-semibold">Saved searches</h1>
                    <div className={"flex mt-0 items-center"}>
                        <p className={"font-light text-sm"}>All saved searches for all groups created by you</p>
                        <div className="tooltip tooltip-right"
                             data-tip="Here are displayed all saved searches created by you for all the groups. To create a new saved search, just go to the group searh for desired content and then press Save search button, the search will then be displayed here.">
                            <FontAwesomeIcon className="ml-1 w-3.5 h-auto" style={{color: "#007BFF"}}
                                             icon={faCircleInfo}/>
                        </div>
                    </div>
                    {savedSearches.length === 0 ? (
                        <div className="flex flex-col h-screen">
                            <div className="flex-grow flex items-center justify-center">
                                <p className="text-xl mb-24">No searches have been saved yet!</p>
                            </div>
                            <div className="flex-grow"></div>
                        </div>
                    ) : (
                        <div className="overflow-x-auto shadow-xl">
                            <div className={"bg-white shadow-xl p-6 rounded-8"}>
                                <table className="table rounded-8 bg-gray-50 ">
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
                                        <tr key={search.id} className={"hover:bg-gray-100 rounded-8"}>
                                            <td className="cursor-pointer"
                                                onClick={() => handleSearchClick(search)}>{index + 1}</td>
                                            <td className="cursor-pointer"
                                                onClick={() => handleSearchClick(search)}>{search.title}</td>
                                            <td className="cursor-pointer"
                                                onClick={() => handleSearchClick(search)}>{search.searchQuery || "/"}</td>
                                            <td className="cursor-pointer"
                                                onClick={() => handleSearchClick(search)}>{search.onTenant}</td>
                                            <td className="cursor-pointer" onClick={() => handleSearchClick(search)}>
                                                {search.sortOrientation}
                                                {search.sortOrientation === 'ASC' ?
                                                    <FontAwesomeIcon className="ml-1 w-2.5 h-auto" icon={faArrowUp}/> :
                                                    <FontAwesomeIcon className="ml-1 w-2.5 h-auto" icon={faArrowDown}/>
                                                }
                                            </td>
                                            <td className="cursor-pointer" onClick={() => handleSearchClick(search)}>
                                                {search.filter.length === 0
                                                    ? '/'
                                                    : search.filter.length > 4
                                                        ? `${search.filter.slice(0, 4).join(', ')} + more`
                                                        : search.filter.join(', ')
                                                }
                                            </td>
                                            <td>
                                                <SavedSearchesPopup icon={faPen} title={"Edit Search"}
                                                                    savedSearch={search} IdToken={props.IdToken}
                                                                    onSavedSearchAction={() => handleSavedSearchAction(props.IdToken)}
                                                                    action={"edit"}/>
                                            </td>
                                            <td>
                                                <SavedSearchesPopup icon={faTrash} title={"Delete Search"}
                                                                    savedSearch={search} IdToken={props.IdToken}
                                                                    onSavedSearchAction={() => handleSavedSearchAction(props.IdToken)}
                                                                    action={"delete"}/>
                                            </td>
                                        </tr>

                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default SavedSearchesTable;