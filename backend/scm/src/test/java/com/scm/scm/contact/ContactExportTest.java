package com.scm.scm.contact;

import com.scm.scm.contact.rest.ContactController;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContactExportTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ExportContactExcel exportContactExcel;

    @Mock
    private UserAccessService userAccessService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExportContactsBadRequest() {
        ExportContactRequest request = new ExportContactRequest(null, null, null);
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testExportContactsForbidden() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId");
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(false);
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testExportContactsInternalServerError() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId");
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(true);
        when(exportContactExcel.exportContacts("tenantUniqueName")).thenThrow(IllegalArgumentException.class);
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testExportContactsOk() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId");
        byte[] exportedContacts = new byte[0];
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(true);
        when(exportContactExcel.exportContacts("tenantUniqueName")).thenReturn(ResponseEntity.ok(exportedContacts));
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exportedContacts, response.getBody());
    }
}