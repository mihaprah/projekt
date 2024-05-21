export interface ExportContactRequest {
    userToken: string;
    tenantUniqueName: string;
    tenantId: string;
    contactIds: string[];
}