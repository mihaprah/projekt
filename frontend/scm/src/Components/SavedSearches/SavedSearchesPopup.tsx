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
    const [requestLoading, setRequestLoading] = useState(false);

    useEffect(() => {
        const fetchTags = async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/unique/${props.savedSearch.onTenant}`, {
                    headers: {
                        'userToken': `Bearer ${props.IdToken}`,
                    },
                });

                if (!res.ok) {
                    toast.error(res.statusText || 'Failed to fetch tags');
                }

                const tenant = await res.json();
                const tags = Object.keys(tenant.contactTags).map(tag => (tag));
                setAvailableTags(tags);
            } catch (error: any) {
                toast.error(error.message || 'Failed to fetch tags');
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

    const handleDelete = async (savedSearchID: string, IdToken: string) => {
        setRequestLoading(true);
        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches/${savedSearchID}`, {
                method: 'DELETE',
                headers: {
                    'userToken': `Bearer ${IdToken}`,
                }
            });
            if (!res.ok) {
                toast.error(res.statusText || "Error to delete search!");
            }
            toast.success("Search deleted successfully!");
            props.onSavedSearchAction();
            setShowPopup(false);
            setRequestLoading(false);
        } catch (error: any) {
            toast.error(error.message || "Failed to delete search!");
            setShowPopup(false);
            setRequestLoading(false);
        }

    }

    const handleEdit = async (savedSearch: PredefinedSearch, IdToken: string) => {
        setRequestLoading(true);
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
                toast.error(res.statusText || "Failed to edit search!")
            }

            toast.success("Search edited successfully!");
            props.onSavedSearchAction();
            setShowPopup(false);
            setRequestLoading(false);
        } catch (error: any) {
            toast.error(error.message || "Failed to edit search!");
            setRequestLoading(false);
            setShowPopup(false);
        }
    }


    return (
        <div>
            <button onClick={() => setShowPopup(true)}
                    className={`btn px-4 btn-sm ${props.action === "delete" ? "bg-danger hover:bg-danger dark:bg-danger dark:hover:bg-danger" : "bg-primary-light hover:bg-primary-light dark:bg-primary-dark dark:hover:bg-primary-dark"} border-0 text-white rounded-8 font-semibold hover:scale-105 transition ml-3`}
            >
                <FontAwesomeIcon className={"w-3.5 h-auto"} icon={props.icon}/>
            </button>
            {showPopup && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-8 rounded-lg w-full max-w-3xl shadow-lg">
                        <h2 className={"text-xl font-semibold"}>{props.title}</h2>
                        {props.action === "delete" && (
                            <>
                                <p className={"font-light text-md mb-4"}>Are you sure you want to delete this search? It
                                    will be deleted permanently!</p>
                                <div className="flex justify-end">
                                    <button
                                        onClick={() => setShowPopup(false)}
                                        className="px-4 py-1 rounded-8 hover:scale-105 transition font-semibold bg-gray-300 text-black mr-2">
                                        Cancel
                                    </button>
                                    {requestLoading ? (
                                        <span className="loading loading-spinner text-primary"></span>
                                    ) : (
                                        <button
                                            onClick={() => handleDelete(savedSearch.id, props.IdToken)}
                                            className="px-4 py-1 font-semibold bg-danger hover:scale-105 transition rounded-8 text-white">
                                            Delete
                                        </button>
                                    )}
                                </div>
                            </>
                        )}
                        {props.action === "edit" && (
                            <>
                                <p className={"font-light text-md"}>Users can edit the title, update the search query, change the sort orientation (ascending or descending), and modify the saved filters from a list of available tags.</p>
                                <div className={"py-2 justify-between flex flex-col w-full"}>
                                    <div className={"my-2"}>
                                        <label className={"text-lg font-semibold mb-2"}>Title</label>
                                        <input
                                            className="rounded-8 text-gray-700 border-1px px-3 w-full mr-3 h-9"
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
                                        <label className={"text-lg font-semibold mb-1"}>Search query</label>
                                        <input
                                            className="rounded-8 text-gray-700 border-1px px-3 w-full mr-3 h-9"
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
                                            <label className="ml-2" htmlFor="asc">Ascending <FontAwesomeIcon
                                                className="ml-1 w-2.5 h-auto" icon={faArrowUp}/>
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
                                            <label className="ml-2" htmlFor="desc">Descending <FontAwesomeIcon
                                                className="ml-1 w-2.5 h-auto" icon={faArrowDown}/>
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
                                            className="appearance-none border-0 rounded w-full py-2 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                        />
                                    </div>
                                </div>
                                <div className={"mt-4 justify-center items-center flex"}>
                                    <button onClick={() => {
                                        setShowPopup(false);
                                    }}
                                            className="btn mt-4 mx-1 px-4 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition"
                                    >Close Popup
                                    </button>
                                    {requestLoading ? (
                                        <span className="loading loading-spinner text-primary"></span>
                                    ) : (
                                        <button onClick={() => handleEdit(savedSearch, props.IdToken)}
                                                className="btn mt-4 mx-1 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                            Save changes
                                        </button>
                                    )}
                                </div>
                            </>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
export default SavedSearchesPopup;