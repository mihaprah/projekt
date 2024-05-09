package com.scm.scm.events.vao;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Random;

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

    public String generateId() {
        final Random random = new Random();
        return System.nanoTime() + "-" + random.nextInt(10_000);
    }

}
