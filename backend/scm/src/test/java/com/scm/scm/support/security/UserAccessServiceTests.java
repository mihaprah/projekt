/**package com.scm.scm.support.security;

import com.scm.scm.config.FirebaseConfig;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserAccessServiceTests {

    @Autowired
    private UserAccessService userAccessService;

    @MockBean
    private TenantRepository tenantRepository;

    @MockBean
    private UserVerifyService userVerifyService;

    @MockBean
    private FirebaseConfig firebaseConfig;

    @BeforeEach
    void setup() {
    }

    @Test
    void testHasAccessToTenant() {
        String tenantId = "123";
        Tenant tenant = new Tenant(tenantId, "123", "123", "description", "colorCode", true, null, null);
        tenant.setUsers(Arrays.asList("user1", "user2"));

        when(tenantRepository.findById(anyString())).thenReturn(java.util.Optional.of(tenant));

        assertTrue(userAccessService.hasAccessToTenant("user1", tenantId));
        assertFalse(userAccessService.hasAccessToTenant("user3", tenantId));
    }

    @Test
    void testHasAccessToTenantWithEmptyUsername() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToTenant("", "123"));
    }

    @Test
    void testHasAccessToTenantWithEmptyTenantId() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToTenant("user1", ""));
    }

    @Test
    void testHasAccessToTenantWithNonExistentTenant() {
        when(tenantRepository.findById("123")).thenReturn(java.util.Optional.empty());

        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToTenant("user1", "123"));
    }

    @Test
    void testHasAccessToContact() {
        String tenantUniqueName = "uniqueName";
        Tenant tenant = new Tenant("123", "123", tenantUniqueName, "description", "colorCode", true, null, null);
        tenant.setUsers(Arrays.asList("user1", "user2"));

        when(tenantRepository.findByTenantUniqueName(anyString())).thenReturn(tenant);

        assertTrue(userAccessService.hasAccessToContact("user1", tenantUniqueName));
        assertFalse(userAccessService.hasAccessToContact("user3", tenantUniqueName));
    }

    @Test
    void testHasAccessToContactWithEmptyUsername() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToContact("", "uniqueName"));
    }

    @Test
    void testHasAccessToContactWithEmptyTenantUniqueName() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToContact("user1", ""));
    }

    @Test
    void testHasAccessToContactWithNonExistentTenant() {
        when(tenantRepository.findByTenantUniqueName("uniqueName")).thenReturn(null);

        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToContact("user1", "uniqueName"));
    }
}*/