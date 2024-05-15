package com.scm.scm.contact;

import com.scm.scm.contact.dto.ContactDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactDTOTests {
    private ContactDTO contactDTO;
    private final Map<String, String> props = Map.of("prop1", "value1", "prop2", "value2");

    @BeforeEach
    public void setUp() {
        contactDTO = new ContactDTO();
    }

    @Test
    void shouldSetAndGetId() {
        contactDTO.setId("testId");
        assertEquals("testId", contactDTO.getId());
    }

    @Test
    void shouldSetAndGetTitle() {
        contactDTO.setTitle("testTitle");
        assertEquals("testTitle", contactDTO.getTitle());
    }

    @Test
    void shouldSetAndGetUser() {
        contactDTO.setUser("testUser");
        assertEquals("testUser", contactDTO.getUser());
    }

    @Test
    void shouldSetAndGetTenantUniqueName() {
        contactDTO.setTenantUniqueName("testTenantUniqueName");
        assertEquals("testTenantUniqueName", contactDTO.getTenantUniqueName());
    }

    @Test
    void shouldSetAndGetComments() {
        contactDTO.setComments("testComments");
        assertEquals("testComments", contactDTO.getComments());
    }

    @Test
    void shouldSetAndGetTags() {
        contactDTO.setTags(Arrays.asList("tag1", "tag2"));
        assertEquals(Arrays.asList("tag1", "tag2"), contactDTO.getTags());
    }

    @Test
    void shouldSetAndGetProps() {
        contactDTO.setProps(props);
        assertEquals(props, contactDTO.getProps());
    }

    @Test
    void shouldSetAndGetAttributesToString() {
        contactDTO.setAttributesToString("testAttributesToString");
        assertEquals("testAttributesToString", contactDTO.getAttributesToString());
    }

    @Test
    void shouldCheckEqualsAndHashCode() {
        ContactDTO contactDTO1 = ContactDTO.builder()
                .id("testId")
                .title("testTitle")
                .user("testUser")
                .tenantUniqueName("testTenantUniqueName")
                .comments("testComments")
                .createdAt("testCreatedAt")
                .tags(Arrays.asList("tag1", "tag2"))
                .props(props)
                .attributesToString("testAttributesToString")
                .build();

        ContactDTO contactDTO2 = ContactDTO.builder()
                .id("testId")
                .title("testTitle")
                .user("testUser")
                .tenantUniqueName("testTenantUniqueName")
                .comments("testComments")
                .createdAt("testCreatedAt")
                .tags(Arrays.asList("tag1", "tag2"))
                .props(props)
                .attributesToString("testAttributesToString")
                .build();

        assertEquals(contactDTO1, contactDTO2);
        assertEquals(contactDTO1.hashCode(), contactDTO2.hashCode());
    }
}