"use client";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowDown, faArrowUp, faFloppyDisk, faList, faRotateLeft, faTh} from "@fortawesome/free-solid-svg-icons";
import React, {useEffect, useState} from "react";
import CreatableSelect from "react-select/creatable";
import {Contact as ContactModel} from "@/models/Contact";
import {Tenant as TenantModel} from "@/models/Tenant";
import {PredefinedSearch as SearchModel, SortOrientation} from "@/models/PredefinedSearch";
import Contacts from "@/Components/Contact/Contacts";
import TenantInfoDisplay from "@/Components/Tenant/TenantInfoDisplay";

interface SearchContactsProps {
    contacts: ContactModel[];
    tenant: TenantModel;
    contactsNumber: number;
    tenantUniqueName: string;
    IdToken: string;
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

const SearchContacts: React.FC<SearchContactsProps> = (props) => {
    const [showAsc, setShowAsc] = useState<boolean>(true);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [tags, setTags] = useState<string[]>([]);
    const availableTags = Object.keys(props.tenant.contactTags).map(tag => ({ label: tag, value: tag }));
    const [contacts, setContacts] = useState<ContactModel[]>(props.contacts);
    const [search, setSearch] = useState<SearchModel>();
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [reset, setReset] = useState<boolean>(false);

    useEffect(() => {
        // Check localStorage for the view mode preference
        const savedViewMode = localStorage.getItem('viewMode');
        if (savedViewMode === 'list' || savedViewMode === 'grid') {
            setViewMode(savedViewMode as 'list' | 'grid');
        }
    }, []);

    const createSearch = (tags: string[], query: string, sortOrientation: SortOrientation) => {
        const newSearch: SearchModel = {
            id: "",
            searchQuery: query || "",
            user: "string",
            onTenant: props.tenant.tenantUniqueName,
            title: "",
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
        const newSearch = createSearch(tags, searchQuery, showAsc ? SortOrientation.ASC : SortOrientation.DESC);
        handleUpdate(newSearch);
    };

    const handleSearchQuery = (query : string) => {
        setSearchQuery(query);
        const newSearch = createSearch(tags || [], query, showAsc ? SortOrientation.ASC : SortOrientation.DESC);
        handleUpdate(newSearch);
    }

    const handleSort = (bool: boolean) => {
        setShowAsc(bool);
        const newSearch = createSearch(tags || [], searchQuery, bool ? SortOrientation.ASC : SortOrientation.DESC);
        handleUpdate(newSearch);
    }

    const handleUpdate = async (search: SearchModel) => {
        const fetchNewContacts = await fetchFilteredContacts(search, props.IdToken, props.tenantUniqueName);
        setContacts(fetchNewContacts);
    }

    const handleSave = () => {

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
        } else {
            setReset(false);
        }
    }

    const handleReset = () => {
        setReset(false);
        setSearchQuery("");
        setTags([]);
        setShowAsc(true);
        const newSearch = createSearch( [], "", SortOrientation.ASC);
        handleUpdate(newSearch);
    }

    return (
        <>
            <TenantInfoDisplay tenant={props.tenant} contactsNumber={props.contactsNumber} onSave={handleContactChange}/>
            <div className={"my-3 flex items-center"}>
                <input value={searchQuery} type="text" placeholder="Search" className="rounded-8 text-gray-700 border px-3 w-96 mr-3 h-9"
                       onChange={(e) => handleSearchQuery(e.target.value)}/>
                <CreatableSelect
                    id="tags"
                    name="tags"
                    isMulti
                    value={tags ? tags.map(tag => ({label: tag, value: tag})) : []}
                    options={availableTags}
                    onChange={handleTagsChange}
                    className="rounded-8 w-96 py-2 px-3"
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

                <button
                    className="btn px-4 mr-3 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark"
                    onClick={handleSave}>
                    Save Search
                    <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faFloppyDisk}/>
                </button>
                <button onClick={toggleViewMode}
                        className="btn mr-3 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                    {viewMode === 'grid' ? (
                        <FontAwesomeIcon icon={faList}/>
                    ) : (
                        <FontAwesomeIcon icon={faTh}/>
                    )}
                </button>
                {reset && (
                    <button
                        onClick={() => handleReset()}
                        className="text-primary-light hover:text-primary-dark transition">
                        reset search <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"} icon={faRotateLeft} />
                    </button>
                )}
            </div>
            <Contacts
                contacts={contacts}
                tenantUniqueName={props.tenantUniqueName}
                IdToken={props.IdToken}
                view={viewMode}
                onDeleted={handleContactChange}
            />
        </>
    );
}

export default SearchContacts;