import {cookies}  from "next/headers";
import Tenant from "../../Components/Tenant/Tenant";
import {Tenant as TenantModel} from "../../models/Tenant";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { redirect } from 'next/navigation'
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


const TenantsPage: React.FC = async () => {
    const IdToken = cookies().get('IdToken')?.value || '';
    if (!IdToken) {
        throw new Error('IdToken is not available');
    }
    const tenants = await fetchTenants(IdToken);

    return (
        <div>
            <div className="flex justify-between items-center">
                <h1 className="text-3xl mx-10 pt-5 text-secondary-dark font-semibold">Tenants</h1>
                <button
                    className="btn mt-4 mx-10 px-5 btn-sm bg-primary-light border-0 text-white dark:bg-primary-dark dark:hover:bg-primary-dark rounded-8 font-semibold hover:scale-105 transition hover:bg-primary-dark">
                    Add new <FontAwesomeIcon className={"text-secondary-light font-semibold"} icon={faPlus}/>
                </button>
            </div>
            <div className="flex space-x-2">
                {tenants.length === 0 ? (
                    <p className="text-center text-2xl mx-auto mt-10">No tenants available!</p>
                ) : (
                    tenants.length === 1 ? (
                        redirect(`http://localhost:3000/contacts/${tenants[0].tenantUniqueName}`)
                    ) : (
                        tenants.map((tenant) => (
                            <Tenant key={tenant.id} tenant={tenant}/>
                        ))
                    )
                )}
            </div>
        </div>
    );
}

export default TenantsPage;