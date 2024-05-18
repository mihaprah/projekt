"use client";
import {Tenant as TenantModel} from '../../models/Tenant';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faUsers} from "@fortawesome/free-solid-svg-icons";
import {useRouter} from "next/navigation";

interface Props {
    tenant: TenantModel;
}

const Tenant: React.FC<Props> = ({tenant}) => {
    const router = useRouter();

    return (
    <div className="card cursor-pointer mx-11 shadow-xl m-7 rounded-8"
         onClick={() => router.push(`/contacts/${tenant.tenantUniqueName}`)}>
        <div className="card-body">
            <h2 className="card-title">{tenant.title}</h2>
            <p>{tenant.description}</p>
            <p><FontAwesomeIcon className={"h-5 w-auto ml-1 mt-8 mr-2"} icon={faUsers}/>{tenant.users.length}</p>
        </div>
        <figure>
            <div style={{backgroundColor: tenant.colorCode, height: '50px', width: '100%'}}></div>
        </figure>
    </div>
    )};

export default Tenant;