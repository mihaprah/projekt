package com.scm.scm.tenant;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.config.FirebaseConfig;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.security.UserVerifyService;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.services.TenantServices;
import com.scm.scm.tenant.rest.TenantController;
import com.scm.scm.support.security.UserAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class TenantControllerTests {

    @Autowired
    private TenantController tenantController;

    @MockBean
    private TenantServices tenantServices;

    @MockBean
    private UserAccessService userAccessService;

    @MockBean
    private UserVerifyService userVerifyService;

    @MockBean
    private FirebaseConfig firebaseConfig;

    @BeforeEach
    public void init() throws Exception {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
        }
    }

    @Test
    void testGetTenant() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        TenantDTO tenantDTO = new TenantDTO();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(true);
        when(tenantServices.getTenantById(tenantId)).thenReturn(tenantDTO);

        ResponseEntity<TenantDTO> response = tenantController.getTenant(tenantId, userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenantDTO, response.getBody());
    }

    @Test
    void testGetTenantsByUser() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        List<TenantDTO> tenants = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(tenantServices.getTenantsByUser(mockToken.getEmail())).thenReturn(tenants);

        ResponseEntity<List<TenantDTO>> response = tenantController.getTenantsByUser(userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenants, response.getBody());
    }

    @Test
    void testCreateTenant() {
        String userToken = "token";
        TenantDTO tenantDTO = new TenantDTO();

        when(tenantServices.addTenant(tenantDTO)).thenReturn(tenantDTO);

        ResponseEntity<TenantDTO> response = tenantController.createTenant(userToken, tenantDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenantDTO, response.getBody());
    }

    @Test
    void testUpdateTenant() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        TenantDTO tenantDTO = new TenantDTO();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantDTO.getId())).thenReturn(true);
        when(tenantServices.updateTenant(tenantDTO)).thenReturn(tenantDTO);

        ResponseEntity<TenantDTO> response = tenantController.updateTenant(userToken, tenantDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenantDTO, response.getBody());
    }

    @Test
    void testDeactivateTenant() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(true);

        ResponseEntity<String> response = tenantController.deactivateTenant(tenantId, userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddUsers() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        List<String> users = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(true);

        ResponseEntity<String> response = tenantController.addUsers(tenantId, userToken, users);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveUsers() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        List<String> users = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(true);

        ResponseEntity<String> response = tenantController.removeUsers(tenantId, userToken, users);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddMultipleTags() {
        String tenantUniqueName = "tenant";
        String tag = "tag";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        String tenantId = "1";
        List<String> contactIds = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(true);

        ResponseEntity<String> response = tenantController.addMultipleTags(tenantUniqueName, new String[]{tag}, userToken, tenantId, contactIds);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetTenantAccessDenied() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantController.getTenant(tenantId, userToken));
    }

    @Test
    void testUpdateTenantAccessDenied() {
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        TenantDTO tenantDTO = new TenantDTO();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantDTO.getId())).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantController.updateTenant(userToken, tenantDTO));
    }

    @Test
    void testDeactivateTenantAccessDenied() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantController.deactivateTenant(tenantId, userToken));
    }

    @Test
    void testAddUsersAccessDenied() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        List<String> users = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantController.addUsers(tenantId, userToken, users));
    }

    @Test
    void testRemoveUsersAccessDenied() {
        String tenantId = "1";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        List<String> users = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantController.removeUsers(tenantId, userToken, users));
    }

    @Test
    void testAddMultipleTagsAccessDenied() {
        String tenantUniqueName = "tenant";
        String tag = "tag";
        String userToken = "Bearer token";
        FirebaseToken mockToken = Mockito.mock(FirebaseToken.class);
        String tenantId = "1";
        List<String> contactIds = Collections.emptyList();

        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""))).thenReturn(mockToken);
        when(userAccessService.hasAccessToTenant(mockToken.getEmail(), tenantId)).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantController.addMultipleTags(tenantUniqueName, new String[]{tag}, userToken, tenantId, contactIds));
    }
}