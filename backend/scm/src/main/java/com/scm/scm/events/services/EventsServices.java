package com.scm.scm.events.services;


import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EventsServices {

    public Event addEvent (Event event){
        event.setId(event.generateId());
        event.setEventTime(LocalDateTime.now());
        if (Objects.equals(event.getPropKey(), "")) {
            throw new IllegalArgumentException("Event propKey is empty");
        }
        if (Objects.equals(event.getCurrentState(), "")) {
            throw new IllegalArgumentException("Event propKey is empty");
        }
        if (Objects.equals(event.getUser(), "")) {
            throw new IllegalArgumentException("Event user is empty");
        }
        if (Objects.equals(event.getContact(), "")) {
            throw new IllegalArgumentException("Event contact is empty");
        }
        if (!checkEnum(event.getEventState())){
            throw new IllegalArgumentException("Event state is not valid");
        }
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
