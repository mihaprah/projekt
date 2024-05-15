package com.scm.scm.loadDatabase;

import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.mongoTemplate.CollectionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;

class LoadEventsTest {

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    LoadEvents loadEvents;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEvent() {
        String tenantUniqueName = "tenant1";
        Event event = new Event("user1", "contact1", EventState.CREATED);

        loadEvents.createEvent(event, tenantUniqueName);

        verify(mongoTemplate, times(1)).save(any(Event.class), eq(tenantUniqueName + CollectionType.ACTIVITY.getCollectionType()));
    }
}