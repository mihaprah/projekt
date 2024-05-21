import {ToastContainer} from "react-toastify";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUsers} from "@fortawesome/free-solid-svg-icons";
import EditTenantPopup from "@/Components/Tenant/EditTenantPopup";
import AddNewContactPopup from "@/Components/Contact/AddNewContactPopup";
import React from "react";
import {Tenant as TenantModel} from "@/models/Tenant";
import TenantSettingsPopup from "@/Components/Tenant/TenantSettingsPopup";

interface TenantInfoDisplayProps {
    tenant: TenantModel;
    contactsNumber: number;
    onSave: () => void;
}

const TenantInfoDisplay: React.FC<TenantInfoDisplayProps> = (props) => {
    return (
        <div>
            <ToastContainer />
            <div className="flex justify-between mb-4">
                <div>
                    <div className={"flex items-center"}>
                        <h2 className="text-3xl font-semibold text-primary-light">{props.tenant.title}</h2>
                        <span className="ml-6 mr-2 text-2xl font-semibold ">{props.contactsNumber}</span>
                        <FontAwesomeIcon className="h-5 w-auto mb-0.5 " icon={faUsers}/>
                    </div>
                    <p className="text-m text-gray-600 mt-1 break-words max-w-96">{props.tenant.description}</p>
                    <div className="mt-4 flex items-center space-x-3">
                        <EditTenantPopup tenant={props.tenant}/>
                        <TenantSettingsPopup tenant={props.tenant} />
                    </div>
                </div>
                <div className="flex items-end space-x-3">
                    <AddNewContactPopup tenantUniqueName={props.tenant.tenantUniqueName} onSave={props.onSave}/>
                </div>
            </div>
        </div>
    );
}

export default TenantInfoDisplay;