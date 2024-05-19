import {cookies}  from "next/headers";
import TenantsDashboard from "@/Components/Tenant/TenantsDashboard";




const TenantsPage: React.FC = async () => {
    const IdToken = cookies().get('IdToken')?.value || '';

    if (!IdToken) {
        throw new Error('IdToken is not available');
    }


    return (
        <div className="container mx-auto p-4">
            <TenantsDashboard IdToken={IdToken} />
        </div>
    );
}

export default TenantsPage;