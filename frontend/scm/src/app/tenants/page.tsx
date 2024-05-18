import {cookies}  from "next/headers";
import TenantsDashboard from "@/Components/Tenant/TenantsDashboard";




const TenantsPage: React.FC = async () => {
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        throw new Error('IdToken is not available');
    }


    return (
        <div>
            <TenantsDashboard IdToken={IdToken} />
        </div>
    );
}

export default TenantsPage;