package com.scm.scm.contact;


import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.contact.services.EventsCheck;
import com.scm.scm.contact.vao.Contact;
import com.scm.scm.events.services.EventsServices;
import com.scm.scm.predefinedSearch.vao.SortOrientation;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.services.TenantServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
class ContactServicesTests {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoTemplateService mongoTemplateService;

    @Mock
    private TenantServices tenantServices;

    @Mock
    private EventsServices eventsServices;

    @Mock
    private EventsCheck eventsCheck;

    @InjectMocks
    private ContactServices contactServices;

    private ContactDTO contactDTO;
    private Contact contact1;
    private Contact contact2;
    private TenantDTO tenantDTO;

    private final List<String> users = Arrays.asList("user1", "user2", "user3");

    private final Map<String, Integer> contactTags = Map.of("tag1", 1, "tag2", 2, "tag3", 3);


    @BeforeEach
    public void init() throws Exception {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
            tenantDTO = TenantDTO.builder().id("id").title("title").tenantUniqueName("tenantUniqueName").description("Short description1").colorCode("#ff4545").active(true).users(users).contactTags(contactTags).build();
            contactDTO = new ContactDTO("idTest", "Updated Title", "user1", "tenantUniqueName", "Updated Comments", LocalDateTime.now().toString(),  Arrays.asList("TagTest1", "TagTest2"), new HashMap<>(), "attributesToString");
            contact1 = new Contact("id1", "Title1", "user1", "tenantUniqueName", "Comments1", LocalDateTime.now(), Arrays.asList("Tag1", "Tag2"), new HashMap<>(), "AttributesToString");
            contact2 = new Contact("id2", "Title2", "user2", "tenantUniqueName", "Comments2", LocalDateTime.now(), Arrays.asList("Tag1", "Tag2"), new HashMap<>(), "AttributesToString");
        }
    }

    @Test
    void testFindOneContact() {
        contact1.setId("1");
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        when(mongoTemplate.findById(anyString(), eq(Contact.class), anyString())).thenReturn(contact1);

        tenantServices.addTenant(tenantDTO);
        ContactDTO result = contactServices.findOneContact("tenantUniqueName", "1");

        assertEquals("1", result.getId());
        verify(mongoTemplateService, times(1)).collectionExists(anyString());
        verify(mongoTemplate, times(1)).findById(anyString(), eq(Contact.class), anyString());
    }

    @Test
    void testFindAllContacts() {
        contact1.setId("1");
        contact2.setId("2");
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        when(mongoTemplate.findAll(eq(Contact.class), anyString())).thenReturn(Arrays.asList(contact1, contact2));

        List<ContactDTO> result = contactServices.findAllContacts("tenantUniqueName");

        assertEquals(2, result.size());
        verify(mongoTemplateService, times(1)).collectionExists(anyString());
        verify(mongoTemplate, times(1)).findAll(eq(Contact.class), anyString());
    }

    @Test
    void testCreateContact() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        when(mongoTemplate.findById(anyString(), eq(Contact.class), anyString())).thenReturn(null);

        String result = contactServices.createContact(contactDTO);

        assertEquals("Contact created successfully to tenantUniqueName_main collection", result);
        verify(mongoTemplate, times(1)).save(any(Contact.class), anyString());
    }

    @Test
    void testUpdateContact() {
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);
        when(mongoTemplate.findById(anyString(), eq(Contact.class), anyString())).thenReturn(contact1);

        ContactDTO updatedContactDTO = contactServices.updateContact(contactDTO);

        assertEquals("Updated Title", updatedContactDTO.getTitle());
        assertEquals("Updated Comments", updatedContactDTO.getComments());

        verify(mongoTemplateService, times(1)).collectionExists(anyString());
        verify(mongoTemplate, times(1)).findById(anyString(), eq(Contact.class), anyString());
        verify(mongoTemplate, times(1)).save(any(Contact.class), anyString());
    }

    @Test
    void testDeleteContact() {
        when(mongoTemplate.findById(anyString(), eq(Contact.class), anyString())).thenReturn(new Contact());
        when(mongoTemplateService.collectionExists(anyString())).thenReturn(true);

        contact1.setId("contactId");

        String result = contactServices.deleteContact("tenantUniqueName", "contactId");

        assertEquals("Contact deleted successfully from tenantUniqueName_main collection", result);
        verify(mongoTemplate, times(1)).remove(any(Contact.class), anyString());
    }

    @Test
    void testGetComparatorBasedOnOrientation() {
        SortOrientation sortOrientation = SortOrientation.ASC;

        Comparator<Contact> result = contactServices.getComparatorBasedOnOrientation(sortOrientation);

        assertNotNull(result);
        assertInstanceOf(Comparator.class, result);
    }
}
