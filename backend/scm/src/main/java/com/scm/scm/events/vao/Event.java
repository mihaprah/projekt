package com.scm.scm.events.vao;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Event {

    @Id
    private String id;
    private String user;
    private String contact;
    private EventState eventState;
    private String propKey;
    private String prevState;
    private String currentState;
    private LocalDateTime eventTime;

    public Event() {
    }

    public Event(String user, String contact, EventState eventState) {
        this.user = user;
        this.contact = contact;
        this.eventState = eventState;
        this.propKey = "";
        this.prevState = "";
        this.currentState = "";
    }

    private static final SecureRandom random = new SecureRandom();
    public String generateId() {
        return System.nanoTime() + "-" + random.nextInt(10_000);
    }

}
