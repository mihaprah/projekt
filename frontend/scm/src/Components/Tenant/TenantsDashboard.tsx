"use client";
import TenantPopup from "@/Components/Tenant/TenantPopup";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import {useRouter} from "next/navigation";
import Tenant from "@/Components/Tenant/Tenant";
import {Tenant as TenantModel} from "@/models/Tenant";
import React, {useEffect, useState} from "react";
import Loading from "@/app/loading";
import {toast} from "react-toastify";

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
            toast.info(res.statusText || 'No tenants created yet');
            return [];
        }

        const tenants = await res.json();

        if (!Array.isArray(tenants)) {
            toast.error('Fetched data is not an array');
            return [];
        }

        return tenants;
    } catch (error: any) {
        toast.info(error.message || 'No tenants created yet');
        return [];
    }
}



const TenantsDashboard: React.FC<TenantDashboardProps> = (props) => {
    const [tenants, setTenants] = useState<TenantModel[]>([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        const fetch = async () => {
            return await fetchTenants(props.IdToken);
        };
        fetch().then((tenantsArray) => {
            setTenants(tenantsArray)
            if(tenantsArray.length === 1){
                router.push(`/contacts/${tenantsArray[0].tenantUniqueName}`);
            }
            setLoading(false);
        });
    }, [props.IdToken, router]);

    const handleTenantAdd = async (IdToken: string) => {
        setTenants(await fetchTenants(IdToken));
    }

    return (
        <div>
            {loading ? (
                <Loading />
            ) : (
                <div>
                    <div className="flex justify-between items-center">
                        <h1 className="text-3xl pt-5 text-secondary-dark font-semibold">Home</h1>
                        <div className={"mt-4"}>
                            <TenantPopup onTenantAdd={() => handleTenantAdd(props.IdToken)} IdToken={props.IdToken}
                                         icon={faPlus}
                                         buttonAction={"Tenant"} title={"Add new Tenant"}
                                         labels={["Title", "Description", "Colour", "Other users"]}
                            />
                        </div>
                    </div>
                    <div>
                        {tenants.length === 0 ? (
                            <div className="flex flex-col h-screen">
                                <div className="flex-grow flex items-center justify-center">
                                    <p className="text-xl mb-24">No tenants have been created yet!</p>
                                </div>
                                <div className="flex-grow"></div>
                            </div>
                        )  : (
                            <div className="grid grid-cols-5 gap-x-4 gap-y-2 mb-6">
                                {tenants.map((tenant) => (
                                    <Tenant key={tenant.id} IdToken={props.IdToken} tenant={tenant}/>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            )}

        </div>
    )
}

export default TenantsDashboard;