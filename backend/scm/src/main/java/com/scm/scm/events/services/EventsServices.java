package com.scm.scm.events.services;


import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import com.scm.scm.support.mongoTemplate.CollectionType;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EventsServices {

    private final MongoTemplate mongoTemplate;
    private final MongoTemplateService mongoTemplateService;

    private static final Logger log = Logger.getLogger(EventsServices.class.toString());

    @Autowired
    public EventsServices(MongoTemplate mongoTemplate, MongoTemplateService mongoTemplateService) {
        this.mongoTemplate = mongoTemplate;
        this.mongoTemplateService = mongoTemplateService;
    }

    public void addEvent (Event event, String tenantUniqueName){
        event.setId(event.generateId());
        event.setEventTime(LocalDateTime.now());
        if (Objects.equals(event.getUser(), "")) {
            log.severe("Event user is empty");
            throw new CustomHttpException("Event user is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (Objects.equals(event.getContact(), "")) {
            log.severe("Event contact is empty");
            throw new CustomHttpException("Event contact is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!checkEnum(event.getEventState())){
            log.severe(ExceptionMessage.EVENT_STATE_NOT_VALID.getExceptionMessage());
            throw new CustomHttpException(ExceptionMessage.EVENT_STATE_NOT_VALID.getExceptionMessage(), 400, ExceptionCause.USER_ERROR);
        }
        log.info("Event created with id: " + event.getId());

        if (mongoTemplateService.collectionExists(tenantUniqueName + CollectionType.ACTIVITY.getCollectionType())) {
            mongoTemplate.save(event, tenantUniqueName + CollectionType.ACTIVITY.getCollectionType());
            log.info("Event " + event.getId() + " saved in collection " + tenantUniqueName + CollectionType.ACTIVITY.getCollectionType());
        }
        else {
            log.severe(ExceptionMessage.COLLECTION_NOT_EXIST.getExceptionMessage());
            throw new CustomHttpException(ExceptionMessage.COLLECTION_NOT_EXIST.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public boolean checkEnum(EventState eventState) {
        log.info("Checking if event state is valid");
        for (EventState event : EventState.values()) {
            if (event == eventState) {
                log.info("Event state is valid");
                return true;
            }
        }
        log.severe(ExceptionMessage.EVENT_STATE_NOT_VALID.getExceptionMessage());
        return false;
    }

    public List<Event> getAllEventsForContact(String contactId, String tenantUniqueName) {
        checkCollection(tenantUniqueName);
        log.log(Level.INFO, "Getting all events for contact: {0} ", contactId);
        return mongoTemplate.findAll(Event.class, tenantUniqueName + CollectionType.ACTIVITY.getCollectionType())
                .stream()
                .filter(event -> event.getContact().equals(contactId))
                .toList();
    }

    public List<Event> getAllEventsForTenant(String tenantUniqueName) {
        checkCollection(tenantUniqueName);
        log.log(Level.INFO, "Getting all events for tenant: {0}", tenantUniqueName);
        return mongoTemplate.findAll(Event.class, tenantUniqueName + CollectionType.ACTIVITY.getCollectionType());
    }

    private void checkCollection(String tenantUniqueName) {
        if (tenantUniqueName == null || tenantUniqueName.isEmpty()) {
            log.severe("Tenant unique name is empty");
            throw new CustomHttpException("Tenant unique name is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + CollectionType.ACTIVITY.getCollectionType())) {
            log.severe(ExceptionMessage.COLLECTION_NOT_EXIST.getExceptionMessage());
            throw new CustomHttpException(ExceptionMessage.COLLECTION_NOT_EXIST.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }
}
