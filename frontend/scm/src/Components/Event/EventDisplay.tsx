import React from 'react';
import { EventState } from "@/models/Event";
import { Event } from "@/models/Event";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircle, faTag, faPen, faPlus, faMinus, faCalendarAlt, faClock } from "@fortawesome/free-solid-svg-icons";

interface EventDisplayProps {
    event: Event;
}

const EventDisplay: React.FC<EventDisplayProps> = ({ event }) => {
    const date = new Date(event.eventTime);
    let formattedDate = `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()}`;
    let formattedTime = `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;

    const getEventIcon = () => {
        switch (event.eventState) {
            case EventState.UPDATED:
                return faPen;
            case EventState.TAG_ADD:
                return faTag;
            case EventState.TAG_REMOVED:
                return faTag;
            case EventState.PROP_ADD:
                return faPlus;
            case EventState.PROP_REMOVED:
                return faMinus;
            default:
                return faCircle;
        }
    };

    const getEventDescription = () => {
        switch (event.eventState) {
            case EventState.UPDATED:
                return `UPDATED ${event.propKey} from "${event.prevState}" to "${event.currentState}"`;
            case EventState.TAG_ADD:
                return `ADDED TAG "${event.currentState}"`;
            case EventState.TAG_REMOVED:
                return `REMOVED TAG "${event.prevState}"`;
            case EventState.PROP_ADD:
                return `ADDED ATTRIBUTE "${event.propKey}" and set it to "${event.currentState}"`;
            case EventState.PROP_REMOVED:
                return `REMOVED ATTRIBUTE "${event.propKey}" with value "${event.prevState}"`;
            default:
                return 'CREATED CONTACT';
        }
    };

    return (
        <div className="py-2 border-b last:border-b-0 border-gray-300">
            <div className="flex items-center">
                <FontAwesomeIcon icon={getEventIcon()} className="text-primary mr-3 ml-1 w-3.5 h-auto" />
                <div className="flex-grow">
                    <span className="text-gray-700 mr-2">{event.user}</span>
                    <span className="text-gray-700">{getEventDescription()}</span>
                </div>
                <div className="flex items-center text-gray-500 ml-2">
                    <FontAwesomeIcon icon={faClock} className="mr-1 ml-1 w-3.5 h-auto" />
                    <span>{formattedTime}</span>
                    <FontAwesomeIcon icon={faCalendarAlt} className="ml-3 mr-1 w-3.5 h-auto" />
                    <span>{formattedDate}</span>
                </div>
            </div>
        </div>
    );
}

const EventList: React.FC<{ events: Event[] }> = ({ events }) => {
    const sortedEvents = [...events].sort((a, b) => new Date(b.eventTime).getTime() - new Date(a.eventTime).getTime());

    return (
        <div>
            {sortedEvents.map((event) => (
                <EventDisplay key={event.id} event={event} />
            ))}
        </div>
    );
}

export default EventList;
