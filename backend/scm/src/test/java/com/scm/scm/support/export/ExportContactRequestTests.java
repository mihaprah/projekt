package com.scm.scm.support.export;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExportContactRequestTests {

    @Test
    public void testGettersAndSetters() {
        ExportContactRequest request = new ExportContactRequest("user1", "uniqueName", "123", (Arrays.asList("contact1", "contact2")));

        assertEquals("user1", request.getUser());
        assertEquals("uniqueName", request.getTenantUniqueName());
        assertEquals("123", request.getTenantId());
        assertEquals(Arrays.asList("contact1", "contact2"), request.getContactIds());
    }

    @Test
    public void testEqualsAndHashCode() {
        ExportContactRequest request1 = new ExportContactRequest("user1", "uniqueName", "123", Arrays.asList("contact1", "contact2"));
        ExportContactRequest request2 = new ExportContactRequest("user1", "uniqueName", "123", Arrays.asList("contact1", "contact2"));

        assertTrue(request1.equals(request2) && request2.equals(request1));
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        ExportContactRequest request = new ExportContactRequest("user1", "uniqueName", "123", Arrays.asList("contact1", "contact2"));
        String expectedString = "ExportContactRequest(user=user1, tenantUniqueName=uniqueName, tenantId=123, contactIds=[contact1, contact2])";

        assertEquals(expectedString, request.toString());
    }
}