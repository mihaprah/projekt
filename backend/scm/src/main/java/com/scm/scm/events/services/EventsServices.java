package com.scm.scm.events.services;


import com.scm.scm.events.vao.EventState;

import com.scm.scm.events.vao.Events;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EventsServices {

    public Events addEvent (String id, String user, String contact, EventState eventState, String propKey, String prevState, String currentState, LocalDateTime eventTime){
        return new Events(id, user, contact, eventState, propKey, prevState, currentState, eventTime);
    }
}
