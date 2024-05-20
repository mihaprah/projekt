export enum SortOrientation {
    ASC = 'ASC',
    DESC = 'DESC',
}

export interface PredefinedSearch {
    id: string;
    searchQuery: string;
    user: string;
    onTenant: string;
    title: string;
    filter: string[];
    sortOrientation: SortOrientation;
}