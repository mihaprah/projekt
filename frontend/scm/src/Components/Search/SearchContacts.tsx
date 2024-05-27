"use client";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowDown, faArrowUp, faCircleInfo, faList, faRotateLeft, faTh} from "@fortawesome/free-solid-svg-icons";
import React, {useEffect, useState} from "react";
import {Contact as ContactModel} from "@/models/Contact";
import {Tenant as TenantModel} from "@/models/Tenant";
import {PredefinedSearch as SearchModel, SortOrientation} from "@/models/PredefinedSearch";
import Contacts from "@/Components/Contact/Contacts";
import TenantInfoDisplay from "@/Components/Tenant/TenantInfoDisplay";
import Select from 'react-select';
import AddSavedSearchPopup from "@/Components/SavedSearches/AddSavedSearchPopup";
import {useRouter} from "next/navigation";

interface SearchContactsProps {
    contacts: ContactModel[];
    tenant: TenantModel;
    contactsNumber: number;
    tenantUniqueName: string;
    IdToken: string;
    numberOfTenants: number;
    searchId?: string;
}

const fetchFilteredContacts = async (search: SearchModel, IdToken: string, tenantUniqueName: string): Promise<ContactModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/search/${tenantUniqueName}`, {
            method: 'PUT',
            headers: {
                'userToken': `Bearer ${IdToken}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(search)
        });

        if (!res.ok) {
            throw new Error(`Error fetching contacts: ${res.statusText}`);
        }

        const contacts = await res.json();

        if (!Array.isArray(contacts)) {
            throw new Error('Fetched data is not an array');
        }

        return contacts;
    } catch (error) {
        console.error('Failed to fetch contacts:', error);
        return [];
    }
}

const fetchAllContacts = async (tenantUniqueName: string, IdToken: string): Promise<ContactModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/${tenantUniqueName}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching contacts: ${res.statusText}`);
        }

        const contacts = await res.json();

        if (!Array.isArray(contacts)) {
            throw new Error('Fetched data is not an array');
        }

        return contacts;
    } catch (error) {
        console.error('Failed to fetch contacts:', error);
        return [];
    }
}

const fetchSearch = async (IdToken: string, searchId: string): Promise<SearchModel> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches/${searchId}`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });
        if (!res.ok) {
            throw new Error(`Error fetching predefined search: ${res.statusText}`);
        }
        const search = await res.json();

        return search as SearchModel;
    } catch (error) {
        console.error('Failed to fetch predefined searches:', error);
        return {} as SearchModel;
    }
}

const SearchContacts: React.FC<SearchContactsProps> = (props) => {
    const [showAsc, setShowAsc] = useState<boolean>(true);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [tags, setTags] = useState<string[]>([]);
    const availableTags = Object.keys(props.tenant.contactTags || {}).map(tag => ({ label: tag, value: tag }));
    const [contacts, setContacts] = useState<ContactModel[]>(props.contacts);
    const [search, setSearch] = useState<SearchModel>();
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [reset, setReset] = useState<boolean>(false);
    const [save, setSave] = useState<boolean>(false);
    const router = useRouter();


    useEffect(() => {
        // Check localStorage for the view mode preference
        const savedViewMode = localStorage.getItem('viewMode');
        if (savedViewMode === 'list' || savedViewMode === 'grid') {
            setViewMode(savedViewMode as 'list' | 'grid');
        }
        if(props.searchId) {
            setPredefinedSearch();
        }
    },[props.tenantUniqueName, props.searchId]);

    const setPredefinedSearch = () => {
        setReset(true);
        setSave(true);
        fetchSearch(props.IdToken, props.searchId!).then(searchModel => {
            setTags(searchModel.filter);
            setSearchQuery(searchModel.searchQuery);
            setShowAsc(searchModel.sortOrientation === SortOrientation.ASC);

            setSearch(searchModel);
            handleUpdate(searchModel);
        });
    }

    const createSearch = (tags: string[], query: string, sortOrientation: SortOrientation, id?: string, user?: string, title?: string) => {
        const newSearch: SearchModel = {
            id: id || "",
            searchQuery: query || "",
            user: user || "",
            onTenant: props.tenant.tenantUniqueName,
            title: title || "",
            filter: tags,
            sortOrientation: sortOrientation,
        };
        checkForReset(tags, query, sortOrientation);
        setSearch(newSearch);
        return newSearch;
    }

    const handleTagsChange = async (selectedOptions: any) => {
        const tags = selectedOptions ? selectedOptions.map((option: any) => option.value) : [];
        setTags(tags);
        const newSearch = createSearch(tags, searchQuery, showAsc ? SortOrientation.ASC : SortOrientation.DESC, search?.id, search?.user, search?.title);
        handleUpdate(newSearch);
    };

    const handleSearchQuery = (query : string) => {
        setSearchQuery(query);
        const newSearch = createSearch(tags || [], query, showAsc ? SortOrientation.ASC : SortOrientation.DESC, search?.id, search?.user, search?.title);
        handleUpdate(newSearch);
    }

    const handleSort = (bool: boolean) => {
        setShowAsc(bool);
        const newSearch = createSearch(tags || [], searchQuery, bool ? SortOrientation.ASC : SortOrientation.DESC, search?.id, search?.user, search?.title);
        handleUpdate(newSearch)
    }

    const handleUpdate = async (search: SearchModel) => {
        const fetchNewContacts = await fetchFilteredContacts(search, props.IdToken, props.tenantUniqueName);
        setContacts(fetchNewContacts);
    }

    const toggleViewMode = () => {
        const newViewMode = viewMode === 'grid' ? 'list' : 'grid';
        setViewMode(newViewMode);
        localStorage.setItem('viewMode', newViewMode);
    };

    const handleContactChange = async () => {
        const fetchContacts = await fetchAllContacts(props.tenantUniqueName, props.IdToken);
        setContacts(fetchContacts);
    }

    const checkForReset = (tags: string[], query: string, sortOrientation: SortOrientation) => {
        if (query !== "" || tags.length > 0 || sortOrientation !== SortOrientation.ASC) {
            setReset(true);
            setSave(true);
        } else {
            setReset(false);
            setSave(false);
        }
    }

    const handleReset = () => {
        if(props.searchId)  {
            router.push(`/contacts/${props.tenantUniqueName}`)
            return;
        }
        setReset(false);
        setSearchQuery("");
        setTags([]);
        setShowAsc(true);
        const newSearch = createSearch( [], "", SortOrientation.ASC);
        handleUpdate(newSearch);
    }

    return (
        <>
            <TenantInfoDisplay tenant={props.tenant} contactsNumber={props.contactsNumber} IdToken={props.IdToken}
                               onSave={handleContactChange} numberOfTenants={props.numberOfTenants}/>
            <div className={"mt-3 flex items-center"}>
                <input value={searchQuery} type="text" placeholder="Search contacts..."
                       className="rounded-8 text-gray-700 border-1px px-3 w-96 mr-3 h-9"
                       onChange={(e) => handleSearchQuery(e.target.value)}/>
                <Select
                    id="tags"
                    name="tags"
                    isMulti
                    value={tags ? tags.map(tag => ({label: tag, value: tag})) : []}
                    options={availableTags}
                    onChange={handleTagsChange}
                    className="rounded-8 w-96 py-1 px-3"
                />
                {showAsc ? (
                    <div>
                        <button className={"bg-primary-light rounded-l-8 text-white px-4 py-1 items-center"}>
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faArrowUp}/>
                        </button>
                        <button className={"border rounded-r-8 px-4 py-1 mr-3 items-center"}
                                onClick={() => handleSort(false)}>
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faArrowDown}/>
                        </button>
                    </div>
                ) : (
                    <div>
                        <button className={"border rounded-l-8 px-4 py-1 items-center"}
                                onClick={() => handleSort(true)}>
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faArrowUp}/>
                        </button>
                        <button className={"bg-primary-light text-white rounded-r-8 px-4 py-1 mr-3 items-center"}>
                            <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faArrowDown}/>
                        </button>
                    </div>
                )}
                <button onClick={toggleViewMode}
                        className="btn mr-3 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                    {viewMode === 'grid' ? (
                        <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faList}/>
                    ) : (
                        <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faTh}/>
                    )}
                </button>
                {save && (
                    <>
                        <AddSavedSearchPopup search={search} IdToken={props.IdToken}/>
                    </>
                )}
                {reset && (
                    <>
                        <button
                            onClick={() => handleReset()}
                            className="text-primary-light hover:text-primary-dark transition">
                            reset search <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faRotateLeft}/>
                        </button>
                    </>
                )}
            </div>
            <div className={"flex mt-0 mb-3 items-center"}>
                <p className={"font-light text-xs"}>Use & and | hover for more info </p>
                <div className="tooltip tooltip-right" data-tip="Use & for AND and | for OR when searching for multiple attributes at the same time. Use without spaces between attributes.">
                    <FontAwesomeIcon className="ml-1 w-3.5 h-auto" style={{color: "#007BFF"}} icon={faCircleInfo}/>
                </div>
            </div>
            {contacts.length !== 0 ? (
                <Contacts
                    contacts={contacts}
                    tenantUniqueName={props.tenantUniqueName}
                    tenantId={props.tenant.id}
                    IdToken={props.IdToken}
                    view={viewMode}
                    onChange={handleContactChange}
                    tenant={props.tenant}
                />
            ) : (
                <div className="flex flex-col mt-20">
                    <div className="flex items-center justify-center">
                        <p className="text-xl mb-24">No contacts created yet!</p>
                    </div>
                </div>
            )}
        </>
    );
}

export default SearchContacts;
