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
import java.util.Collections;

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
    public void init() throws Exception{
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)){}
    }

    @Test
    public void testExportContactsBadRequest() {
        ExportContactRequest request = new ExportContactRequest(null, null, null, Collections.emptyList());
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testExportContactsForbidden() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId", Collections.emptyList());
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(false);
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testExportContactsNoContent() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId", Collections.emptyList());
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(true);
        when(userAccessService.hasAccessToContact("user", "tenantUniqueName")).thenReturn(true);
        when(exportContactExcel.exportContacts("tenantUniqueName", Collections.emptyList())).thenReturn(ResponseEntity.noContent().build());
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testExportContactsOk() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId", Collections.emptyList());
        byte[] exportedContacts = new byte[0];
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(true);
        when(userAccessService.hasAccessToContact("user", "tenantUniqueName")).thenReturn(true);
        when(exportContactExcel.exportContacts("tenantUniqueName", Collections.emptyList())).thenReturn(ResponseEntity.ok(exportedContacts));
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exportedContacts, response.getBody());
    }
}