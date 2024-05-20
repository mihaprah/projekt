export interface Contact {
    id: string;
    title: string;
    user: string;
    tenantUniqueName: string;
    comments: string;
    createdAt?: Date;
    tags: string[];
    props: Record<string, string>;
    attributesToString: string;
}