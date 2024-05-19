export enum EventState {
    CREATED = 'CREATED',
    UPDATED = 'UPDATED',
    DELETED = 'DELETED',
    TAG_ADD = 'TAG_ADD',
    TAG_REMOVED = 'TAG_REMOVED',
    PROP_ADD = 'PROP_ADD',
    PROP_REMOVED = 'PROP_REMOVED'
}

export interface Event {
    id: string;
    user: string;
    contact: string;
    eventState: EventState;
    propKey: string;
    prevState: string;
    currentState: string;
    eventTime: Date;
}