"use client";
import React, {useEffect, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {PredefinedSearch, PredefinedSearch as SavedSearchesModel} from "@/models/PredefinedSearch";
import {IconDefinition} from "@fortawesome/fontawesome-svg-core";
import {faArrowDown, faArrowUp} from "@fortawesome/free-solid-svg-icons";
import {toast} from "react-toastify";
import Select from "react-select";

interface SavedSearchesPopupProps {
    icon: IconDefinition;
    title: string;
    IdToken: string;
    savedSearch: SavedSearchesModel;
    onSavedSearchAction: () => void;
    action: string;
}

const SavedSearchesPopup: React.FC<SavedSearchesPopupProps> = (props) => {
    const [showPopup, setShowPopup] = useState(false);
    const [savedSearch, setSavedSearch] = useState(props.savedSearch);
    const [availableTags, setAvailableTags] = useState<string[]>();

    useEffect(() => {
        const fetchTags = async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/unique/${props.savedSearch.onTenant}`, {
                    headers: {
                        'userToken': `Bearer ${props.IdToken}`,
                    },
                });

                if (!res.ok) {
                    throw new Error(`Error fetching tags: ${res.statusText}`);
                }

                const tenant = await res.json();
                const tags = Object.keys(tenant.contactTags).map(tag => (tag));
                setAvailableTags(tags);
            } catch (error) {
                console.error('Failed to fetch tags:', error);
            }
        };
            fetchTags();
    }, [props.savedSearch.onTenant, props.IdToken]);

    const handleTagsChange = (selectedOption: any) => {
        setSavedSearch(values => {
            return {...values, filter: selectedOption.map((option: any) => option.value)};
        });
    };

    const handleInputChange = (key: string, newValue: string) => {
        setSavedSearch(values => {
            return {...values, [key]: newValue};
        });
    };

    const handleDelete = async (savedSearchID: string, IdToken:  string) => {
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches/${savedSearchID}`, {
                method: 'DELETE',
                headers: {
                    'userToken': `Bearer ${IdToken}`,
                }
            });
            if (!res.ok) {
                toast.error("Error to delete search!");
                throw new Error(`Error deleting search: ${res.statusText}`);
            }
            toast.success("Search deleted successfully!");
            props.onSavedSearchAction();
            setShowPopup(false);
        } catch (error) {
            toast.error("Failed to delete search!");
        }

    }

    const handleEdit = async (savedSearch: PredefinedSearch, IdToken: string) => {
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                },
                body: JSON.stringify(savedSearch),
            });
            if (!res.ok) {
                throw new Error(`Error editing search: ${res.statusText}`);
            }

            toast.success("Search edited successfully!");
            props.onSavedSearchAction();
            setShowPopup(false);

        } catch (error) {
            toast.error("Failed to edit search!");
        }
    }


    return (
        <div>
            <button onClick={() => setShowPopup(true)}
                    className={`btn px-4 btn-sm ${props.action === "delete" ? "bg-danger hover:bg-danger dark:bg-danger dark:hover:bg-danger" : "bg-primary-light hover:bg-primary-light"} text-white rounded-8 font-semibold hover:scale-105 transition`}
            >
                <FontAwesomeIcon className={"w-3.5 h-auto"} icon={props.icon}/>
            </button>
            {showPopup && (
                <div
                    className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-60 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg">
                        <h2 className={"font-semibold mb-4 text-2xl"}>{props.title}</h2>
                        {props.action === "delete" && (<p>Are you sure you want to delete this search?</p>)}
                        {props.action === "edit" && (
                            <div className={"p-2 justify-between flex flex-col w-400px"}>
                                <div className={"my-2"}>
                                    <label className={"text-lg font-semibold mb-2"}>Title</label>
                                    <input
                                        className="rounded-8 text-gray-700 border-1px px-3 w-96 mr-3 h-9"
                                        type="text"
                                        value={savedSearch.title}
                                        onChange={(e) => handleInputChange("title", e.target.value)}
                                    />
                                </div>
                                <div className={"mt-4"}>
                                    <label className={"text-lg font-semibold mb-2"}>On Tenant</label>
                                    <p className={"text-md font-normal"}>
                                        {savedSearch.onTenant}
                                    </p>
                                </div>
                                <div className={"mt-4"}>
                                    <label className={"font-semibold my-3"}>Search query</label>
                                    <input
                                        className="rounded-8 text-gray-700 border-1px px-3 w-96 mr-3 h-9"
                                        type="text"
                                        value={savedSearch.searchQuery}
                                        onChange={(e) => handleInputChange("searchQuery", e.target.value)}
                                    />
                                </div>
                                <div className={"mt-4"}>
                                    <label className={"text-lg font-semibold mb-1"}>Orientation</label>
                                    <div className="flex items-center mt-2">
                                        <input
                                            type="radio"
                                            id="asc"
                                            name="orientation"
                                            value="ASC"
                                            checked={savedSearch.sortOrientation === 'ASC'}
                                            onChange={(e) => handleInputChange("sortOrientation", e.target.value)}
                                        />
                                        <label className="ml-2" htmlFor="asc">Ascending <FontAwesomeIcon className="ml-1 w-2.5 h-auto" icon={faArrowUp}/>
                                        </label>
                                    </div>
                                    <div className="flex items-center mt-2">
                                        <input
                                            type="radio"
                                            id="desc"
                                            name="orientation"
                                            value="DESC"
                                            checked={savedSearch.sortOrientation === 'DESC'}
                                            onChange={(e) => handleInputChange("sortOrientation", e.target.value)}
                                        />
                                        <label className="ml-2" htmlFor="desc">Descending <FontAwesomeIcon className="ml-1 w-2.5 h-auto" icon={faArrowDown}/>
                                        </label>
                                    </div>
                                </div>
                                <div className={"mt-4"}>
                                    <label className={"text-lg font-semibold mb-1"}>Filter</label>
                                    <Select
                                        isMulti
                                        value={savedSearch.filter.map(tag => ({label: tag, value: tag}))}
                                        options={availableTags?.map(tag => ({label: tag, value: tag}))}
                                        onChange={handleTagsChange}
                                        className="appearance-none border-0 mb-10 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    />
                                </div>

                            </div>
                        )}
                        <div className={"mt-4 justify-center items-center flex"}>
                            <button onClick={() => {
                                setShowPopup(false);
                            }}
                                    className="btn mt-4 mx-1 px-4 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition"
                            >Close Popup
                            </button>
                            {props.action === "delete" ?
                                <button onClick={() => handleDelete(savedSearch.id, props.IdToken)}
                                        className="btn mt-4 mx-1 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Delete Search
                                </button> :
                                <button onClick={() => handleEdit(savedSearch, props.IdToken)}
                                    className="btn mt-4 mx-1 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Save changes
                                </button>}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
export default SavedSearchesPopup;