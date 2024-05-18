export interface Tenant {
    id: string;
    title: string;
    tenantUniqueName: string;
    description: string;
    colorCode: string;
    active?: boolean;
    users: string[];
    contactTags: Record<string, number>;
    labels: Record<string, string>;
    displayProps: string[];
}