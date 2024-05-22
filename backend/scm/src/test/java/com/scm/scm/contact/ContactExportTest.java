package com.scm.scm.contact;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.support.security.UserVerifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ExportContactExcelTest {

    @Mock
    private ContactServices contactServices;

    @Mock
    private UserVerifyService userVerifyService;

    @Mock
    private UserAccessService userAccessService;

    @InjectMocks
    private ExportContactExcel exportContactExcel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportContactsBadRequest() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(anyString())).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(anyString(), anyString())).thenReturn(true);
        when(userAccessService.hasAccessToContact(anyString(), anyString())).thenReturn(true);

        ExportContactRequest request = new ExportContactRequest(null, null, Collections.emptyList());
        ExportContactExcel exportContactExcel = new ExportContactExcel(contactServices);
        ResponseEntity<byte[]> response = exportContactExcel.exportContacts("tenantUniqueName", Collections.emptyList());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testExportContactsForbidden() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(anyString())).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(anyString(), anyString())).thenReturn(false);
        when(userAccessService.hasAccessToContact(anyString(), anyString())).thenReturn(false);

        ExportContactRequest request = new ExportContactRequest("tenantUniqueName", "tenantId", Collections.emptyList());
        ExportContactExcel exportContactExcel = new ExportContactExcel(contactServices);
        ResponseEntity<byte[]> response = exportContactExcel.exportContacts("tenantUniqueName", Collections.emptyList());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
