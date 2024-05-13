package com.scm.scm.contact;

import com.scm.scm.contact.vao.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactTests {
    private Contact contact;
    private final Map<String, String> props = Map.of("prop1", "value1", "prop2", "value2");

    @BeforeEach
    void setUp() {
        contact = new Contact();
    }

    @Test
    public void shouldGenerateId() {
        String id = contact.generateId("TestTitle");
        assertNotNull(id);
        assertTrue(id.startsWith("testtitle-"));
    }

    @Test
    public void shouldContactAttributesToString() {
        contact.setTitle("TestTitle");
        contact.setTags(Arrays.asList("tag1", "tag2"));
        contact.setProps(props);

        String attributesToString = contact.contactAttributesToString();
        assertEquals("testtitle,tag1,tag2,prop1,prop2,", attributesToString);
    }

    @Test
    public void shouldSetAndGetId() {
        contact.setId("testId");
        assertEquals("testId", contact.getId());
    }

    @Test
    public void shouldSetAndGetTitle() {
        contact.setTitle("testTitle");
        assertEquals("testTitle", contact.getTitle());
    }

    @Test
    public void shouldSetAndGetUser() {
        contact.setUser("testUser");
        assertEquals("testUser", contact.getUser());
    }

    @Test
    public void shouldSetAndGetTenantUniqueName() {
        contact.setTenantUniqueName("testTenantUniqueName");
        assertEquals("testTenantUniqueName", contact.getTenantUniqueName());
    }

    @Test
    public void shouldSetAndGetComments() {
        contact.setComments("testComments");
        assertEquals("testComments", contact.getComments());
    }

    @Test
    public void shouldSetAndGetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        contact.setCreatedAt(now);
        assertEquals(now, contact.getCreatedAt());
    }

    @Test
    public void shouldSetAndGetTags() {
        contact.setTags(Arrays.asList("tag1", "tag2"));
        assertEquals(Arrays.asList("tag1", "tag2"), contact.getTags());
    }

    @Test
    public void shouldSetAndGetProps() {
        contact.setProps(props);
        assertEquals(props, contact.getProps());
    }

    @Test
    public void shouldSetAndGetAttributesToString() {
        contact.setAttributesToString("testAttributesToString");
        assertEquals("testAttributesToString", contact.getAttributesToString());
    }
}
