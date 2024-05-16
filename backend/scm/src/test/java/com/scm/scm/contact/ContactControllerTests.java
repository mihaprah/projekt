package com.scm.scm.contact;

import com.scm.scm.config.FirebaseConfig;
import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.rest.ContactController;
import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.support.security.UserVerifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContactControllerTests {

    @Autowired
    private ContactController contactController;

    @MockBean
    private ContactServices contactServices;

    @MockBean
    private ExportContactExcel exportContactExcel;

    @MockBean
    private UserAccessService userAccessService;

    @MockBean
    private UserVerifyService userVerifyService;

    @MockBean
    private FirebaseConfig firebaseConfig;

    @MockBean
    private PredefinedSearchServices predefinedSearchServices;

    @BeforeEach
    public void init() throws Exception{
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)){
        }
    }

    @Test
    void testGetContact() throws IOException {
        String id = "1";
        String tenantUniqueName = "tenant";
        String userToken = "token";
        ContactDTO contactDTO = new ContactDTO();
        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(true);
        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
        when(contactServices.findOneContact(tenantUniqueName, id)).thenReturn(contactDTO);
        ResponseEntity<ContactDTO> response = contactController.getContact(id, tenantUniqueName, userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contactDTO, response.getBody());
    }

//    @Test
//    void testGetContacts() {
//        String tenantUniqueName = "tenant";
//        String userToken = "token";
//        List<ContactDTO> contacts = Collections.emptyList();
//        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(true);
//        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
//        when(contactServices.findAllContacts(tenantUniqueName)).thenReturn(contacts);
//        ResponseEntity<List<ContactDTO>> response = contactController.getContacts(tenantUniqueName, userToken);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(contacts, response.getBody());
//    }

    @Test
    void testAddContact() {
        String userToken = "token";
        ContactDTO contactDTO = new ContactDTO();
        when(userAccessService.hasAccessToContact(userToken, contactDTO.getTenantUniqueName())).thenReturn(true);
        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
        ResponseEntity<String> response = contactController.addContact(userToken, contactDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateContact() {
        String userToken = "token";
        ContactDTO contactDTO = new ContactDTO();
        when(userAccessService.hasAccessToContact(userToken, contactDTO.getTenantUniqueName())).thenReturn(true);
        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
        ResponseEntity<ContactDTO> response = contactController.updateContact(userToken, contactDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteContact() {
        String id = "1";
        String tenantUniqueName = "tenant";
        String userToken = "token";
        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(true);
        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
        ResponseEntity<String> response = contactController.deleteContact(id, tenantUniqueName, userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchContacts() {
        String tenantUniqueName = "tenant";
        String userToken = "token";
        PredefinedSearchDTO searchDTO = new PredefinedSearchDTO();
        PredefinedSearch search = new PredefinedSearch();
        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(true);
        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
        when(predefinedSearchServices.convertToEntity(searchDTO)).thenReturn(search);
        ResponseEntity<List<ContactDTO>> response = contactController.searchContacts(tenantUniqueName, userToken, searchDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetContactAccessDenied() {
        String id = "1";
        String tenantUniqueName = "tenant";
        String userToken = "token";
        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(false);
        when(userVerifyService.verifyUserToken(userToken)).thenReturn(null);
        assertThrows(CustomHttpException.class, () -> contactController.getContact(id, tenantUniqueName, userToken));
    }

    @Test
    void testExportContactsBadRequest() {
        ExportContactRequest request = null;
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testExportContactsAccessDenied() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId", List.of("contactId"));
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(false);
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testExportContactsInternalServerError() {
        ExportContactRequest request = new ExportContactRequest("user", "tenantUniqueName", "tenantId", List.of("contactId"));
        when(userAccessService.hasAccessToTenant("user", "tenantId")).thenReturn(true);
        when(userAccessService.hasAccessToContact("user", "tenantUniqueName")).thenReturn(true);
        when(exportContactExcel.exportContacts("tenantUniqueName", List.of("contactId"))).thenThrow(IllegalArgumentException.class);
        ResponseEntity<byte[]> response = contactController.exportContacts(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAddContactAccessDenied() {
        String userToken = "token";
        ContactDTO contactDTO = new ContactDTO();
        when(userAccessService.hasAccessToContact(userToken, contactDTO.getTenantUniqueName())).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> contactController.addContact(userToken, contactDTO));
    }

    @Test
    void testUpdateContactAccessDenied() {
        String userToken = "token";
        ContactDTO contactDTO = new ContactDTO();
        when(userAccessService.hasAccessToContact(userToken, contactDTO.getTenantUniqueName())).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> contactController.updateContact(userToken, contactDTO));
    }

    @Test
    void testDeleteContactAccessDenied() {
        String id = "1";
        String tenantUniqueName = "tenant";
        String userToken = "token";
        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> contactController.deleteContact(id, tenantUniqueName, userToken));
    }

    @Test
    void testSearchContactsAccessDenied() {
        String tenantUniqueName = "tenant";
        String userToken = "token";
        PredefinedSearchDTO searchDTO = new PredefinedSearchDTO();
        when(userAccessService.hasAccessToContact(userToken, tenantUniqueName)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> contactController.searchContacts(tenantUniqueName, userToken, searchDTO));
    }
}