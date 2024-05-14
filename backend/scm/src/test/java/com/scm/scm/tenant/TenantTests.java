package com.scm.scm.tenant;

import com.scm.scm.tenant.vao.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TenantTests {

    private Tenant tenant;

    private final List<String> users = Arrays.asList("user1", "user2", "user3");
    private final Map<String, Integer> contactTags = Map.of("tag1", 1, "tag2", 2, "tag3", 3);

    @BeforeEach
    void setUp() {
        tenant = new Tenant("id", "title", "tenantUniqueName", "Short description", "#ff4545", true, users, null);
    }

    @Test
    void shouldInstantiateTenant() {
        assertNotNull(tenant);
    }

    @Test
    void shouldGenerateTenantUniqueName() {
        String uniqueName = tenant.generateTenantUniqueName(tenant.getTitle());
        assertNotNull(uniqueName);
        System.out.println(uniqueName);
        assertTrue(uniqueName.matches("[A-Z]{3}-[A-Z][a-z]{2}-\\d{2}-\\d{1,3}"));
    }

    @Test
    void shouldThrowExceptionForShortTenantTitle() {
        assertThrows(IllegalArgumentException.class, () -> tenant.generateTenantUniqueName("Te"));
    }

    @Test
    void shouldGenerateId() {
        String id = tenant.generateId("Test Tenant");
        assertNotNull(id);
        assertTrue(id.matches("testtenant-\\d+-\\d{1,4}"));
    }

    @Test
    void shouldSetAndGetContactTags() {
        tenant.setContactTags(contactTags);
        assertEquals(contactTags, tenant.getContactTags());
    }
}