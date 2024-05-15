package com.scm.scm.loadDatabase;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.events.vao.Event;
import com.scm.scm.tenant.services.TenantServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LoadContactsTest {

    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    TenantServices tenantServices;

    @Mock
    LoadEvents loadEvents;

    @InjectMocks
    LoadContacts loadContacts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createContacts() {
        String[] tenantUniqueNames = {"tenant1", "tenant2"};
        loadContacts.createContacts(tenantUniqueNames);

        verify(mongoTemplate, atLeast(5)).save(any(Contact.class), anyString());
        verify(loadEvents, atLeast(5)).createEvent(any(Event.class), anyString());
        verify(tenantServices, atLeast(1)).addTags(anyString(), anyList());
    }

    @Test
    void selectRandomTags() {
        List<String> tags = new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5"));
        List<String> selectedTags = loadContacts.selectRandomTags(tags, 3);

        assertNotNull(selectedTags);
        assertEquals(3, selectedTags.size());
    }

    @Test
    void selectRandomProps() {
        Map<String, String> props = new HashMap<>();
        props.put("prop1", "value1");
        props.put("prop2", "value2");
        props.put("prop3", "value3");
        props.put("prop4", "value4");
        props.put("prop5", "value5");

        Map<String, String> selectedProps = loadContacts.selectRandomProps(props, 3);

        assertNotNull(selectedProps);
        assertEquals(3, selectedProps.size());
    }
}