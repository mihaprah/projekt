package com.scm.scm.contact;

import com.scm.scm.contact.services.EventsCheck;
import com.scm.scm.contact.vao.Contact;
import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.tenant.services.TenantServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EventsCheckTests {

    @InjectMocks
    private EventsCheck eventsCheck;

    @Mock
    private EventsServices eventsServices;

    @Mock
    private TenantServices tenantServices;

    @Captor
    ArgumentCaptor<Event> eventCaptor;

    @BeforeEach
    public void init() throws Exception {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {}
    }

    @Test
    void testCheckProps() {
        Contact existingContact = new Contact();
        existingContact.setProps(new HashMap<>(Collections.singletonMap("key1", "value1")));

        Contact newContact = new Contact();
        newContact.setProps(new HashMap<>(Collections.singletonMap("key1", "value2")));

        String username = "username";

        eventsCheck.checkProps(existingContact, newContact, username);

        verify(eventsServices).addEvent(eventCaptor.capture(), eq(existingContact.getTenantUniqueName()));
        Event capturedEvent = eventCaptor.getValue();
        assertEquals(EventState.UPDATED, capturedEvent.getEventState());
        assertEquals("key1", capturedEvent.getPropKey());
        assertEquals("value1", capturedEvent.getPrevState());
        assertEquals("value2", capturedEvent.getCurrentState());
    }

    @Test
    void testCheckTags() {
        Contact existingContact = new Contact();
        existingContact.setTags(Collections.singletonList("tag1"));

        Contact newContact = new Contact();
        newContact.setTags(Collections.singletonList("tag2"));

        String username = "username";

        eventsCheck.checkTags(existingContact, newContact, username);

        verify(eventsServices, times(2)).addEvent(eventCaptor.capture(), eq(existingContact.getTenantUniqueName()));
        Event capturedEvent = eventCaptor.getAllValues().getFirst();
        assertEquals(EventState.TAG_REMOVED, capturedEvent.getEventState());
        assertEquals("TAG", capturedEvent.getPropKey());
        assertEquals("tag1", capturedEvent.getPrevState());
        assertEquals("", capturedEvent.getCurrentState());
    }
}