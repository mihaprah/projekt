import {EventState} from "@/models/Event";
import {Event} from "@/models/Event";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCircle} from "@fortawesome/free-solid-svg-icons";

interface EventDisplayProps {
    event: Event;
}

const EventDisplay: React.FC<EventDisplayProps> = (props) => {
    const date = new Date(props.event.eventTime);
    let formattedDate = `${date.getDate()}-${date.getMonth()+1}-${date.getFullYear()}`;
    let formattedHours = `${date.getHours()}:${date.getMinutes() < 10 ? '0' : ''}${date.getMinutes()}`;

    return (
        <div className={"py-1"}>
            {props.event.eventState === EventState.UPDATED && (
                <div className={"flex items-center"}>
                    <FontAwesomeIcon icon={faCircle} className={"m-1 h-2 w-auto"}/>
                    <p className={"m-1"}>{props.event.user}</p>
                    <p className={"m-1"}>UPDATED</p>
                    <p className={"m-1 font-semibold"}>{props.event.propKey}</p>
                    <p className={"m-1"}>from</p>
                    <p className={"m-1 font-semibold"}>{props.event.prevState}</p>
                    <p className={"m-1"}>to</p>
                    <p className={"m-1 font-semibold"}>{props.event.currentState}</p>
                    <p className={"m-1"}>at {formattedHours}</p>
                    <p className={"m-1"}>on {formattedDate}</p>
                </div>
            )}
            {props.event.eventState === EventState.TAG_ADD && (
                <div className={"flex items-center"}>
                    <FontAwesomeIcon icon={faCircle} className={"m-1 h-2 w-auto"}/>
                    <p className={"m-1"}>{props.event.user}</p>
                    <p className={"m-1"}>ADDED TAG</p>
                    <p className={"m-1 font-semibold"}>{props.event.currentState}</p>
                    <p className={"m-1"}>at {formattedHours}</p>
                    <p className={"m-1"}>on {formattedDate}</p>
                </div>
            )}
            {props.event.eventState === EventState.TAG_REMOVED && (
                <div className={"flex items-center"}>
                    <FontAwesomeIcon icon={faCircle} className={"m-1 h-2 w-auto"}/>
                    <p className={"m-1"}>{props.event.user}</p>
                    <p className={"m-1"}>REMOVED TAG</p>
                    <p className={"m-1 font-semibold"}>{props.event.prevState}</p>
                    <p className={"m-1"}>at {formattedHours}</p>
                    <p className={"m-1"}>on {formattedDate}</p>
                </div>
            )}
            {props.event.eventState === EventState.PROP_ADD && (
                <div className={"flex items-center"}>
                    <FontAwesomeIcon icon={faCircle} className={"m-1 h-2 w-auto"}/>
                    <p className={"m-1"}>{props.event.user}</p>
                    <p className={"m-1"}>ADDED ATTRIBUTE</p>
                    <p className={"m-1 font-semibold"}>{props.event.propKey}</p>
                    <p className={"m-1"}>and set it to</p>
                    <p className={"m-1 font-semibold"}>{props.event.currentState}</p>
                    <p className={"m-1"}>at {formattedHours}</p>
                    <p className={"m-1"}>on {formattedDate}</p>
                </div>
            )}
            {props.event.eventState === EventState.PROP_REMOVED && (
                <div className={"flex items-center"}>
                    <FontAwesomeIcon icon={faCircle} className={"m-1 h-2 w-auto"}/>
                    <p className={"m-1"}>{props.event.user}</p>
                    <p className={"m-1"}>REMOVED ATTRIBUTE</p>
                    <p className={"m-1 font-semibold"}>{props.event.propKey}</p>
                    <p className={"m-1"}>and it's value</p>
                    <p className={"m-1 font-semibold"}>{props.event.prevState}</p>
                    <p className={"m-1"}>at {formattedHours}</p>
                    <p className={"m-1"}>on {formattedDate}</p>
                </div>
            )}
        </div>
    );
}

export default EventDisplay;