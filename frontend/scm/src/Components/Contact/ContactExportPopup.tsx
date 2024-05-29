import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faDownload} from "@fortawesome/free-solid-svg-icons";
import React, {useState} from "react";
import {toast} from "react-toastify";
import {useRouter} from "next/navigation";
import 'react-toastify/dist/ReactToastify.css';
import {ExportContactRequest} from "@/models/ExportContactRequest";

interface ContactExportPopupProps extends ExportContactRequest{
    IdToken: string;
}


const ContactExportPopup: React.FC<ContactExportPopupProps> = (props) => {
    const router = useRouter();
    const [showPopup, setShowPopup] = useState(false);
    const [requestLoading, setRequestLoading] = useState<boolean>(false);

    const handleExport = async () => {
        setRequestLoading(true);
        try {
            if (props.contactIds.length === 0) {
                toast.error("No contacts selected to export.");
                setShowPopup(false);
                return;
            }
            const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/contacts/export`, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    'userToken': `Bearer ${props.IdToken}`,
                },
                body: JSON.stringify({
                    tenantUniqueName: props.tenantUniqueName,
                    tenantId: props.tenantId,
                    contactIds: props.contactIds,
                }),
            });

            if (!res.ok) {
                toast.error(res.statusText || "Failed to export contacts.");
            }

            const blob = await res.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');

            const now = new Date();
            const date = now.toLocaleDateString();
            const time = now.toLocaleTimeString();

            a.href = url;
            a.download = props.tenantUniqueName + '_contacts_' + date + '_' + time + '.xlsx';
            document.body.appendChild(a);
            a.click();
            a.remove();

            toast.success("Contacts exported successfully!");
            setShowPopup(false);
            setRequestLoading(false);
            router.refresh();
        } catch (error: any) {
            toast.error(error.message || "Failed to export contacts.");
            setShowPopup(false);
            setRequestLoading(false);
        }
    }


    return (
        <div>
        <button  onClick={() => setShowPopup(true)} type="button"
                className="btn px-4 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
            Export
            <FontAwesomeIcon className="ml-1 w-3.5 h-auto" icon={faDownload}/>
        </button>
            {showPopup && (
                <div className="fixed z-20 flex flex-col justify-center items-center bg-gray-500 bg-opacity-65 inset-0">
                    <div className="bg-white p-10 rounded-8 shadow-lg max-w-3xl w-full">
                        <h2 className="font-semibold text-2xl">Export contacts</h2>
                        <p className={"font-light text-md mb-4"}>Are you sure you want to export selected contacts in excel format?</p>
                        <div className="mt-4 flex justify-center items-center">
                            <button onClick={() => setShowPopup(false)}
                                    className="btn mt-4 mx-1 px-5 btn-sm bg-danger border-0 text-white rounded-8 font-semibold hover:bg-danger hover:scale-105 transition">
                                Close Popup
                            </button>
                            {requestLoading ? (
                                <span className="loading loading-spinner text-primary"></span>
                            ) : (
                                <button type="button" onClick={handleExport}
                                        className="btn mt-4 mx-1 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:bg-primary-light hover:scale-105 transition">
                                    Export Contacts
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    )


}

export default ContactExportPopup;