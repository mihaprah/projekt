package com.scm.scm.loadDatabase;


import com.scm.scm.events.vao.Event;
import com.scm.scm.support.mongoTemplate.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class LoadEvents {

    private static final Logger log = Logger.getLogger(LoadEvents.class.toString());

    private final MongoTemplate mongoTemplate;

    @Autowired
    public LoadEvents(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void createEvent(Event event, String tenantUniqueName) {
        event.setId(event.generateId());
        event.setEventTime(LocalDateTime.now());
        mongoTemplate.save(event, tenantUniqueName + CollectionType.ACTIVITY.getCollectionType());
        log.info("Event saved into the database.");
    }
}
