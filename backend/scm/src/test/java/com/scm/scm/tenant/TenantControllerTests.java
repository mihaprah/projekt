package com.scm.scm.tenant;

import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.services.TenantServices;
import com.scm.scm.tenant.rest.TenantController;
import com.scm.scm.support.security.UserAccessService;
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

    @BeforeEach
    public void init() throws Exception{
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)){}
    }

    @Test
    void testGetTenants() {
        List<TenantDTO> tenants = Collections.emptyList();
        when(tenantServices.getAllTenants()).thenReturn(tenants);
        ResponseEntity<List<TenantDTO>> response = tenantController.getTenants();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenants, response.getBody());
    }

    @Test
    void testGetTenant() {
        String tenantId = "1";
        String userToken = "token";
        TenantDTO tenantDTO = new TenantDTO();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        when(tenantServices.getTenantById(tenantId)).thenReturn(tenantDTO);
        ResponseEntity<TenantDTO> response = tenantController.getTenant(tenantId, userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenantDTO, response.getBody());
    }

    @Test
    void testGetTenantsByUser() {
        String userToken = "token";
        List<TenantDTO> tenants = Collections.emptyList();
        when(tenantServices.getTenantsByUser(userToken)).thenReturn(tenants);
        ResponseEntity<List<TenantDTO>> response = tenantController.getTenantsByUser(userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenants, response.getBody());
    }

    @Test
    void testCreateTenant() {
        TenantDTO tenantDTO = new TenantDTO();
        when(tenantServices.addTenant(tenantDTO)).thenReturn(tenantDTO);
        ResponseEntity<TenantDTO> response = tenantController.createTenant(tenantDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenantDTO, response.getBody());
    }

    @Test
    void testUpdateTenant() {
        String userToken = "token";
        TenantDTO tenantDTO = new TenantDTO();
        when(userAccessService.hasAccessToTenant(userToken, tenantDTO.getId())).thenReturn(true);
        when(tenantServices.updateTenant(tenantDTO)).thenReturn(tenantDTO);
        ResponseEntity<TenantDTO> response = tenantController.updateTenant(userToken, tenantDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tenantDTO, response.getBody());
    }

    @Test
    void testDeactivateTenant() {
        String tenantId = "1";
        String userToken = "token";
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        ResponseEntity<String> response = tenantController.deactivateTenant(tenantId, userToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddTag() {
        String tenantId = "1";
        String userToken = "token";
        List<String> tags = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        ResponseEntity<String> response = tenantController.addTag(tenantId, userToken, tags);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveTag() {
        String tenantId = "1";
        String userToken = "token";
        List<String> tags = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        ResponseEntity<String> response = tenantController.removeTag(tenantId, userToken, tags);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddUsers() {
        String tenantId = "1";
        String userToken = "token";
        List<String> users = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        ResponseEntity<String> response = tenantController.addUsers(tenantId, userToken, users);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveUsers() {
        String tenantId = "1";
        String userToken = "token";
        List<String> users = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        ResponseEntity<String> response = tenantController.removeUsers(tenantId, userToken, users);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddMultipleTags() {
        String tenantUniqueName = "tenant";
        String tag = "tag";
        String userToken = "token";
        String tenantId = "1";
        List<String> contactIds = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(true);
        ResponseEntity<String> response = tenantController.addMultipleTags(tenantUniqueName, tag, userToken, tenantId, contactIds);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetTenantAccessDenied() {
        String tenantId = "1";
        String userToken = "token";
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.getTenant(tenantId, userToken));
    }

    @Test
    void testUpdateTenantAccessDenied() {
        String userToken = "token";
        TenantDTO tenantDTO = new TenantDTO();
        when(userAccessService.hasAccessToTenant(userToken, tenantDTO.getId())).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.updateTenant(userToken, tenantDTO));
    }

    @Test
    void testDeactivateTenantAccessDenied() {
        String tenantId = "1";
        String userToken = "token";
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.deactivateTenant(tenantId, userToken));
    }

    @Test
    void testAddTagAccessDenied() {
        String tenantId = "1";
        String userToken = "token";
        List<String> tags = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.addTag(tenantId, userToken, tags));
    }

    @Test
    void testRemoveTagAccessDenied() {
        String tenantId = "1";
        String userToken = "token";
        List<String> tags = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.removeTag(tenantId, userToken, tags));
    }

    @Test
    void testAddUsersAccessDenied() {
        String tenantId = "1";
        String userToken = "token";
        List<String> users = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.addUsers(tenantId, userToken, users));
    }

    @Test
    void testRemoveUsersAccessDenied() {
        String tenantId = "1";
        String userToken = "token";
        List<String> users = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.removeUsers(tenantId, userToken, users));
    }

    @Test
    void testAddMultipleTagsAccessDenied() {
        String tenantUniqueName = "tenant";
        String tag = "tag";
        String userToken = "token";
        String tenantId = "1";
        List<String> contactIds = Collections.emptyList();
        when(userAccessService.hasAccessToTenant(userToken, tenantId)).thenReturn(false);
        assertThrows(CustomHttpException.class, () -> tenantController.addMultipleTags(tenantUniqueName, tag, userToken, tenantId, contactIds));
    }
}