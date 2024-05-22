"use client";
import TenantPopup from "@/Components/Tenant/TenantPopup";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import {redirect} from "next/navigation";
import Tenant from "@/Components/Tenant/Tenant";
import {Tenant as TenantModel} from "@/models/Tenant";
import {useEffect, useState} from "react";

interface TenantDashboardProps {
    IdToken: string;
}

const fetchTenants = async (IdToken: string): Promise<TenantModel[]> => {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/tenants/user`, {
            headers: {
                'userToken': `Bearer ${IdToken}`,
            },
        });

        if (!res.ok) {
            throw new Error(`Error fetching tenants: ${res.statusText}`);
        }

        const tenants = await res.json();

        if (!Array.isArray(tenants)) {
            throw new Error('Fetched data is not an array');
        }

        return tenants;
    } catch (error) {
        console.error('Failed to fetch tenants:', error);
        return [];
    }
}



const TenantsDashboard: React.FC<TenantDashboardProps> = (props) => {
    const [tenants, setTenants] = useState<TenantModel[]>([]);

    useEffect(() => {
        const fetch = async () => {
            const fetchedTenants = await fetchTenants(props.IdToken);
            setTenants(fetchedTenants);
        };
        fetch();
    }, [props.IdToken]);

    const handleTenantAdd = async (IdToken: string) => {
        setTenants(await fetchTenants(IdToken));
    }

    return (
        <div>
            <div className="flex justify-between items-center">
                <h1 className="text-3xl pt-5 text-secondary-dark font-semibold">Tenants</h1>
                <div className={"mt-4"}>
                    <TenantPopup onTenantAdd={() => handleTenantAdd(props.IdToken)} IdToken={props.IdToken} icon={faPlus}
                                 buttonAction={"Add new"} title={"Add new Tenant"}
                                 labels={["Title", "Description", "Colour", "Other users"]}
                                 />
                </div>
            </div>
            <div>
                {tenants.length === 0 ? (
                    <p className="text-center text-2xl mx-auto mt-10">No tenants available!</p>
                ) : (
                    tenants.length === 1 ? (
                        redirect(`http://localhost:3000/contacts/${tenants[0].tenantUniqueName}`)
                    ) : (
                        <div className="grid grid-cols-5 gap-x-4 gap-y-2 mb-6">
                            {tenants.map((tenant) => (
                                <Tenant key={tenant.id} IdToken={props.IdToken} tenant={tenant}/>
                            ))}
                        </div>
                    )
                )}
            </div>
        </div>
    )
}

export default TenantsDashboard;