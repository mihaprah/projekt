import {toast} from "react-toastify";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import React, {useEffect, useState} from "react";
import {faFloppyDisk} from "@fortawesome/free-solid-svg-icons";
import {PredefinedSearch as SearchModel, SortOrientation} from "@/models/PredefinedSearch";

interface AddSavedSearchPopupProps {
    search: SearchModel | undefined;
    IdToken: string;
}

const addSavedSearch = async (search: SearchModel, IdToken: string): Promise<void> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'userToken': `Bearer ${IdToken}`,
            },
            body: JSON.stringify(search),
        });

        if (!res.ok) {
            toast.error(res.statusText || "Failed to save search!");
        } else {
            toast.success("Search saved successfully!");
        }
    } catch (error: any) {
        toast.error(error.message || "Failed to save search!");
    }
}

const updateSavedSearch = async (search: SearchModel, IdToken: string): Promise<void> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'userToken': `Bearer ${IdToken}`,
            },
            body: JSON.stringify(search),
        });
        if (!res.ok) {
            toast.error(res.statusText || "Failed to update search!");
        } else {
            toast.success("Search updated successfully!");
        }
    } catch (error: any) {
        toast.error(error.message || "Failed to update search!");
    }
}


const AddSavedSearchPopup: React.FC<AddSavedSearchPopupProps> = (props) => {
    const [showPopup, setShowPopup] = useState(false);
    const [title, setTitle] = useState<string>("");


    useEffect(() => {
        if (props.search?.title && props.search?.id) {
            setTitle(props.search.title);
        }
    }, [props.search?.title]);

    const handleSave = async () => {
        if (!title) {
            toast.error("Title is required!");
            return;
        }
        props.search!.title = title;
        if (props.search?.id) {
            updateSavedSearch(props.search!, props.IdToken).then(() => {
                setShowPopup(false);
            });
        } else {
            addSavedSearch(props.search!, props.IdToken).then(() => {
                setTitle("");
                setShowPopup(false);
            });
        }

    };

    const handleTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTitle(e.target.value);
    };

    return (
        <div>
            <button onClick={() => setShowPopup(true)}
                    className="btn mr-3 px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                {props.search?.id ? 'Update Search' : 'Save Search'} <FontAwesomeIcon className={"ml-1 w-3.5 h-auto"}
                                                                                      icon={faFloppyDisk}/>
            </button>

            {showPopup && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg w-full max-w-3xl">
                        <h2 className={"font-semibold mb-4 text-2xl"}> {props.search?.id ? 'Update Search' : 'Save Search'}</h2>
                        <div className={"flex flex-col"}>
                            <div className={"my-3"}>
                                <label className={"font-semibold mb-1"}>Title</label>
                                <input value={title} type={"text"} placeholder={"Search title"}
                                       className="rounded-8 text-gray-700 border-1px px-3 w-full mr-3 h-9"
                                       onChange={handleTitle}/>
                            </div>
                            {props.search?.searchQuery && (
                                <div className={"my-3"}>
                                    <label className={"font-semibold mb-1"}>Search Query</label>
                                    <p className="text-gray-600 break-words max-w-96">{props.search.searchQuery}</p>
                                </div>
                            )}
                            {props.search?.filter && (
                                <div className={"my-3"}>
                                    <label className={"font-semibold mb-1"}>Search Filters (TAGS)</label>
                                    <p className="text-gray-600 break-words max-w-96">{props.search!.filter.join(", ")}</p>
                                </div>
                            )}
                            {props.search?.sortOrientation === SortOrientation.ASC ? (
                                <div className={"my-3"}>
                                    <label className={"font-semibold mb-1"}>Orientation</label>
                                    <p className="text-gray-600 break-words max-w-96">Ascending</p>
                                </div>
                            ) : (
                                <div className={"my-3"}>
                                    <label className={"font-semibold mb-1"}>Orientation</label>
                                    <p className="text-gray-600 break-words max-w-96">Descending</p>
                                </div>

                            )}
                        </div>

                        <div className={"mt-4 justify-center items-center flex"}>
                            <button onClick={() => setShowPopup(false)}
                                    className="btn mt-4 mx-1 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition"
                            >Close Popup
                            </button>
                            <button onClick={() => handleSave()}
                                    className="btn  mt-4 mx-1 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                {props.search?.id ? 'Update Search' : 'Save Search'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AddSavedSearchPopup;