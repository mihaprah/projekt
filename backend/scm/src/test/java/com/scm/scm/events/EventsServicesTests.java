package com.scm.scm.events;


import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class EventsServicesTests {

    @InjectMocks
    private EventsServices eventsServices;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoTemplateService mongoTemplateService;

    private Event event;

    @BeforeEach
    public void init() throws Exception {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
            event = new Event("id", "user1", "contact1", EventState.CREATED, "propKey", "prevState", "currentState", LocalDateTime.now());
        }
    }

    /**@Test
    void shouldAddEvent() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        eventsServices.addEvent(event, "tenantUniqueName");
        verify(mongoTemplate, times(1)).save(event, "tenantUniqueName" + CollectionType.ACTIVITY.getCollectionType());
    }*/

    @Test
    void shouldThrowExceptionWhenCollectionDoesNotExist() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> eventsServices.addEvent(event, "tenantUniqueName"));
    }

    @Test
    void shouldCheckEnum() {
        assertTrue(eventsServices.checkEnum(EventState.CREATED));
        assertFalse(eventsServices.checkEnum(null));
    }

    @Test
    void shouldThrowExceptionWhenUserIsEmpty() {
        event.setUser("");
        assertThrows(CustomHttpException.class, () -> eventsServices.addEvent(event, "tenantUniqueName"));
    }

    @Test
    void shouldThrowExceptionWhenContactIsEmpty() {
        event.setContact("");
        assertThrows(CustomHttpException.class, () -> eventsServices.addEvent(event, "tenantUniqueName"));
    }

    @Test
    void shouldThrowExceptionWhenEventStateIsInvalid() {
        event.setEventState(null);
        assertThrows(CustomHttpException.class, () -> eventsServices.addEvent(event, "tenantUniqueName"));
    }

    @Test
    void shouldThrowExceptionWhenTenantUniqueNameDoesNotCorrespondToExistingCollection() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> eventsServices.addEvent(event, "nonExistentTenantUniqueName"));
    }

    /**@Test
    void shouldGetAllEventsForContact() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        when(mongoTemplate.findAll(Event.class, "tenantUniqueName" + CollectionType.ACTIVITY.getCollectionType())).thenReturn(Collections.singletonList(event));
        List<Event> events = eventsServices.getAllEventsForContact("contact1", "tenantUniqueName");
        assertEquals(1, events.size());
        assertEquals(event, events.getFirst());
    }

    @Test
    void shouldGetAllEventsForTenant() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        when(mongoTemplate.findAll(Event.class, "tenantUniqueName" + CollectionType.ACTIVITY.getCollectionType())).thenReturn(Collections.singletonList(event));
        List<Event> events = eventsServices.getAllEventsForTenant("tenantUniqueName");
        assertEquals(1, events.size());
        assertEquals(event, events.getFirst());
    }*/

    @Test
    void shouldThrowExceptionWhenTenantUniqueNameIsEmptyForGetAllEventsForContact() {
        assertThrows(CustomHttpException.class, () -> eventsServices.getAllEventsForContact("contact1", ""));
    }

    @Test
    void shouldThrowExceptionWhenTenantUniqueNameIsEmptyForGetAllEventsForTenant() {
        assertThrows(CustomHttpException.class, () -> eventsServices.getAllEventsForTenant(""));
    }

}
