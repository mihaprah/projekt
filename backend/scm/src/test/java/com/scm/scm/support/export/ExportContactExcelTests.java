/**package com.scm.scm.support.export;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExportContactExcelTests {

    @MockBean
    private ContactServices contactServices;

    @Autowired
    private ExportContactExcel exportContactExcel;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportContacts() {
        String contactId1 = "1";
        String contactId2 = "2";
        ContactDTO contact1 = new ContactDTO(contactId1, "Title1", "User1", "Tenant1", "Comments1", "2022-01-01", List.of("Tag1"), Collections.emptyMap(), "Attributes1");
        ContactDTO contact2 = new ContactDTO(contactId2, "Title2", "User2", "Tenant2", "Comments2", "2022-01-02", List.of("Tag2"), Collections.emptyMap(), "Attributes2");
        List<ContactDTO> contacts = List.of(contact1, contact2);
        when(contactServices.findAllContacts("Tenant")).thenReturn(contacts);

        ResponseEntity<byte[]> response = exportContactExcel.exportContacts("Tenant", List.of(contactId1, contactId2));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Objects.requireNonNull(response.getHeaders().getContentType()).toString());
    }

    @Test
    void testExportContactsNoContacts() {
        when(contactServices.findAllContacts("Tenant")).thenReturn(Collections.emptyList());

        ResponseEntity<byte[]> response = exportContactExcel.exportContacts("Tenant", List.of("1"));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}*/