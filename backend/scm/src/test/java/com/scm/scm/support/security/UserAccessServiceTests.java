package com.scm.scm.support.security;

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
public class UserAccessServiceTests {

    @Autowired
    private UserAccessService userAccessService;

    @MockBean
    private TenantRepository tenantRepository;

    @BeforeEach
    public void setup() {
        // No need to open mocks manually when using @MockBean
    }

    @Test
    public void testHasAccessToTenant() {
        String tenantId = "123";
        Tenant tenant = new Tenant(tenantId, "123", "123", "description", "colorCode", true, null, null);
        tenant.setUsers(Arrays.asList("user1", "user2"));

        when(tenantRepository.findById(anyString())).thenReturn(java.util.Optional.of(tenant));

        assertTrue(userAccessService.hasAccessToTenant("user1", tenantId));
        assertFalse(userAccessService.hasAccessToTenant("user3", tenantId));
    }

    @Test
    public void testHasAccessToTenantWithEmptyUsername() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToTenant("", "123"));
    }

    @Test
    public void testHasAccessToTenantWithEmptyTenantId() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToTenant("user1", ""));
    }

    @Test
    public void testHasAccessToTenantWithNonExistentTenant() {
        when(tenantRepository.findById("123")).thenReturn(java.util.Optional.empty());

        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToTenant("user1", "123"));
    }

    @Test
    public void testHasAccessToContact() {
        String tenantUniqueName = "uniqueName";
        Tenant tenant = new Tenant("123", "123", tenantUniqueName, "description", "colorCode", true, null, null);
        tenant.setUsers(Arrays.asList("user1", "user2"));

        when(tenantRepository.findByTenantUniqueName(anyString())).thenReturn(tenant);

        assertTrue(userAccessService.hasAccessToContact("user1", tenantUniqueName));
        assertFalse(userAccessService.hasAccessToContact("user3", tenantUniqueName));
    }

    @Test
    public void testHasAccessToContactWithEmptyUsername() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToContact("", "uniqueName"));
    }

    @Test
    public void testHasAccessToContactWithEmptyTenantUniqueName() {
        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToContact("user1", ""));
    }

    @Test
    public void testHasAccessToContactWithNonExistentTenant() {
        when(tenantRepository.findByTenantUniqueName("uniqueName")).thenReturn(null);

        assertThrows(CustomHttpException.class, () -> userAccessService.hasAccessToContact("user1", "uniqueName"));
    }
}