package com.scm.scm.contact;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.config.FirebaseConfig;
import com.scm.scm.contact.rest.ContactController;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.support.security.UserVerifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContactExportTest {

    @Autowired
    private ContactController contactController;

    @Mock
    private ExportContactExcel exportContactExcel;

    @MockBean
    private UserAccessService userAccessService;

    @Mock
    private UserVerifyService userVerifyService;

    @MockBean
    private FirebaseConfig firebaseConfig;


    @BeforeEach
    public void init() throws Exception{
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)){}
    }

    @Test
    void testExportContactsBadRequest() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(anyString())).thenReturn(mockToken);
        when(userAccessService.hasAccessToContact(mockToken.getEmail(), null)).thenReturn(true);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), null)).thenReturn(true);

        ExportContactRequest request = new ExportContactRequest(null, null, Collections.emptyList());
        ResponseEntity<byte[]> response = contactController.exportContacts(request, userToken);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testExportContactsForbidden() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(anyString())).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), "tenantId")).thenReturn(false);
        when(userAccessService.hasAccessToContact(mockToken.getEmail(), null)).thenReturn(false);

        ExportContactRequest request = new ExportContactRequest( "tenantUniqueName", "tenantId", Collections.emptyList());
        ResponseEntity<byte[]> response = contactController.exportContacts(request, userToken);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}