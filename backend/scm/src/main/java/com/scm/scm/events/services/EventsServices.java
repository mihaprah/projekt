package com.scm.scm.events.services;


import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class EventsServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoTemplateService mongoTemplateService;

    private static final Logger log = Logger.getLogger(EventsServices.class.toString());

    public void addEvent (Event event, String tenantUniqueName){
        event.setId(event.generateId());
        event.setEventTime(LocalDateTime.now());
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

        if (mongoTemplateService.collectionExists(tenantUniqueName + "_activity")) {
            mongoTemplate.save(event, tenantUniqueName + "_activity");
            log.info("Event " + event.getId() + " saved in collection " + tenantUniqueName + "_activity");
        }
        else {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
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
