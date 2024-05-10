package com.scm.scm.events.services;


import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class EventsServices {

    private static final Logger log = Logger.getLogger(EventsServices.class.toString());

    public Event addEvent (Event event){
        event.setId(event.generateId());
        event.setEventTime(LocalDateTime.now());
        if (Objects.equals(event.getPropKey(), "")) {
            throw new CustomHttpException("Event propKey is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (Objects.equals(event.getCurrentState(), "")) {
            throw new CustomHttpException("Event current state is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (Objects.equals(event.getUser(), "")) {
            throw new CustomHttpException("Event user is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (Objects.equals(event.getContact(), "")) {
            throw new CustomHttpException("Event contact is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!checkEnum(event.getEventState())){
            throw new CustomHttpException("Event state is not valid", 400, ExceptionCause.USER_ERROR);
        }
        log.info("Event created with id: " + event.getId());
        return event;
    }

    private boolean checkEnum(EventState eventState) {
        for (EventState event : EventState.values()) {
            if (event == eventState) {
                return true;
            }
        }
        return false;
    }
}
