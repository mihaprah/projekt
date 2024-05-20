"use client";
import {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {PredefinedSearch, PredefinedSearch as SavedSearchesModel} from "@/models/PredefinedSearch";
import {IconDefinition} from "@fortawesome/fontawesome-svg-core";

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
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState("Error editing search");
    const [alertType, setAlertType] = useState("Error editing search");


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
                throw new Error(`Error deleting search: ${res.statusText}`);
            }
            setAlertMessage("Search deleted successfully!");
            setAlertType("alert-success");
            setShowAlert(true);
            setTimeout(() => {
                props.onSavedSearchAction();
                setShowPopup(false);
                setShowAlert(false);
            }, 1500);

        } catch (error) {
            console.error('Failed to delete search:', error);
        }

    }

    const handleEdit = async (savedSearch: PredefinedSearch, IdToken: string) => {
        const filter = savedSearch.filter.toString();
        const filterArray: string[] = filter.split(",");
        const updatedSearch = {...savedSearch, filter: filterArray};

        try {
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/predefined_searches`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'userToken': `Bearer ${IdToken}`,
                },
                body: JSON.stringify(updatedSearch),
            });
            if (!res.ok) {
                throw new Error(`Error editing search: ${res.statusText}`);
            }
            setAlertMessage("Search edited successfully!");
            setAlertType("alert-success");
            setShowAlert(true);
            setTimeout(() => {
                props.onSavedSearchAction();
                setShowPopup(false);
                setShowAlert(false);
            }, 1500);

        } catch (error) {
            console.error('Failed to edit search:', error);
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
                            <div className={"p-2 justify-between flex flex-col items-start"}>
                                <label className={"font-normal mb-1"}>Title</label>
                                <p className={"text-lg font-semibold mb-2"}>
                                    {savedSearch?.title}
                                </p>
                                <label className={"font-normal mb-1"}>On Tenant</label>
                                <p className={"text-lg font-semibold mb-2"}>
                                    {savedSearch?.onTenant}
                                </p>
                                <label className={"font-normal mb-1"}>Search query</label>
                                <input
                                    className={"input input-bordered w-60 mb-2"}
                                    type="text"
                                    value={savedSearch?.searchQuery}
                                    onChange={(e) => handleInputChange("searchQuery", e.target.value)}
                                />
                                <label className={"font-normal mb-1"}>Orientation</label>
                                <select
                                    className={"input input-bordered w-60 mb-2"}
                                    value={savedSearch?.sortOrientation}
                                    onChange={(e) => handleInputChange("sortOrientation", e.target.value)}
                                >
                                    <option value='ASC'>ASC</option>
                                    <option value='DESC'>DESC</option>
                                </select>
                                <label className={"font-normal mb-1"}>Filter</label>
                                <textarea
                                    className={"input input-bordered w-60 mb-2"}
                                    value={savedSearch.filter}
                                    onChange={(e) => {
                                        handleInputChange("filter", e.target.value)
                                    }}
                                />
                            </div>
                        )}
                        <div className={"mt-4 justify-center items-center flex"}>
                            <button onClick={() => setShowPopup(false)}
                                    className="btn mt-4 mx-3 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition"
                            >Close Popup
                            </button>
                            {props.action === "delete" ?
                                <button onClick={() => handleDelete(savedSearch.id, props.IdToken)}
                                        className="btn mt-4 mx-3 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Delete
                                </button> :
                                <button onClick={() => handleEdit(savedSearch, props.IdToken)}
                                    className="btn mt-4 mx-3 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Save
                                </button>}

                        </div>
                    </div>
                    {showAlert && (
                        <div role="alert" className={`${alertType} alert fixed bottom-0 w-full`}>
                            <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6"
                                 fill="none" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                      d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                            </svg>
                            <span>{alertMessage}.</span>
                        </div>
                    )}
                </div>
            )}
        </div>
    );

}

export default SavedSearchesPopup;