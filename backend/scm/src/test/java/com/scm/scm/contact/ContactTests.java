package com.scm.scm.contact;

import com.scm.scm.contact.vao.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactTests {
    private Contact contact;
    private final Map<String, String> props = Map.of("prop1", "value1", "prop2", "value2");

    @BeforeEach
    public void setUp() {
        contact = new Contact();
    }

    @Test
    void shouldGenerateId() {
        String id = contact.generateId("TestTitle");
        assertNotNull(id);
        assertTrue(id.startsWith("testtitle-"));
    }

    @Test
    void shouldSetAndGetId() {
        contact.setId("testId");
        assertEquals("testId", contact.getId());
    }

    @Test
    void shouldSetAndGetTitle() {
        contact.setTitle("testTitle");
        assertEquals("testTitle", contact.getTitle());
    }

    @Test
    void shouldSetAndGetUser() {
        contact.setUser("testUser");
        assertEquals("testUser", contact.getUser());
    }

    @Test
    void shouldSetAndGetTenantUniqueName() {
        contact.setTenantUniqueName("testTenantUniqueName");
        assertEquals("testTenantUniqueName", contact.getTenantUniqueName());
    }

    @Test
    void shouldSetAndGetComments() {
        contact.setComments("testComments");
        assertEquals("testComments", contact.getComments());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        contact.setCreatedAt(now);
        assertEquals(now, contact.getCreatedAt());
    }

    @Test
    void shouldSetAndGetTags() {
        contact.setTags(Arrays.asList("tag1", "tag2"));
        assertEquals(Arrays.asList("tag1", "tag2"), contact.getTags());
    }

    @Test
    void shouldSetAndGetProps() {
        contact.setProps(props);
        assertEquals(props, contact.getProps());
    }

    @Test
    void shouldSetAndGetAttributesToString() {
        contact.setAttributesToString("testAttributesToString");
        assertEquals("testAttributesToString", contact.getAttributesToString());
    }

    @Test
    void shouldContactAttributesToString() {
        contact.setTitle("TestTitle");
        contact.setTags(Arrays.asList("tag1", "tag2"));

        Map<String, String> orderedProps = new LinkedHashMap<>();
        orderedProps.put("prop1", "value1");
        orderedProps.put("prop2", "value2");
        contact.setProps(orderedProps);

        String attributesToString = contact.contactAttributesToString();

        assertEquals("testtitle,tag1,tag2,prop1,prop2,value1,value2,", attributesToString);
    }

    @Test
    void shouldBuildContact() {
        LocalDateTime now = LocalDateTime.now();
        Contact builtContact = Contact.builder()
                .id("testId")
                .title("testTitle")
                .user("testUser")
                .tenantUniqueName("testTenantUniqueName")
                .comments("testComments")
                .createdAt(now)
                .tags(Arrays.asList("tag1", "tag2"))
                .props(props)
                .attributesToString("testAttributesToString")
                .build();

        assertEquals("testId", builtContact.getId());
        assertEquals("testTitle", builtContact.getTitle());
        assertEquals("testUser", builtContact.getUser());
        assertEquals("testTenantUniqueName", builtContact.getTenantUniqueName());
        assertEquals("testComments", builtContact.getComments());
        assertEquals(now, builtContact.getCreatedAt());
        assertEquals(Arrays.asList("tag1", "tag2"), builtContact.getTags());
        assertEquals(props, builtContact.getProps());
        assertEquals("testAttributesToString", builtContact.getAttributesToString());
    }

    @Test
    void shouldCheckEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Contact contact1 = Contact.builder()
                .id("testId")
                .title("testTitle")
                .user("testUser")
                .tenantUniqueName("testTenantUniqueName")
                .comments("testComments")
                .createdAt(now)
                .tags(Arrays.asList("tag1", "tag2"))
                .props(props)
                .attributesToString("testAttributesToString")
                .build();

        Contact contact2 = Contact.builder()
                .id("testId")
                .title("testTitle")
                .user("testUser")
                .tenantUniqueName("testTenantUniqueName")
                .comments("testComments")
                .createdAt(now)
                .tags(Arrays.asList("tag1", "tag2"))
                .props(props)
                .attributesToString("testAttributesToString")
                .build();

        assertEquals(contact1, contact2);
        assertEquals(contact1.hashCode(), contact2.hashCode());
    }
}
