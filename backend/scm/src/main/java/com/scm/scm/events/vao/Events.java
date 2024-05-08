package com.scm.scm.events.vao;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Events {

    @Id
    private String id;
    private String user;
    private String contact;
    private EventState eventState;
    private String propKey;
    private String prevState;
    private String currentState;
    private LocalDateTime eventTime;





}
