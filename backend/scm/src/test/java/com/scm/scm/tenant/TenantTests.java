package com.scm.scm.tenant;

import com.scm.scm.tenant.vao.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TenantTests {

    @Test
    void testGenerateTenantUniqueName() {
        Tenant tenant = Tenant.builder().build();
        String tenantUniqueName = tenant.generateTenantUniqueName("TestTenant");
        assertNotNull(tenantUniqueName);
        assertTrue(tenantUniqueName.matches("[A-Z]{3}-[A-Z][a-z]{2}-\\d{2}-\\d{1,3}"));
    }

    @Test
    void testGenerateTenantUniqueNameShortTitle() {
        Tenant tenant = Tenant.builder().build();
        assertThrows(IllegalArgumentException.class, () -> tenant.generateTenantUniqueName("Te"));
    }

    @Test
    void testGenerateId() {
        Tenant tenant = Tenant.builder().build();
        String id = tenant.generateId("TestTenant");
        assertNotNull(id);
        assertTrue(id.matches("testtenant-\\d+-\\d{1,4}"));
    }

    @Test
    void testSetTitleAndGetTitle() {
        Tenant tenant = Tenant.builder().build();
        String title = "New Title";
        tenant.setTitle(title);
        assertEquals(title, tenant.getTitle());
    }

    @Test
    void testSetTenantUniqueNameAndGetTenantUniqueName() {
        Tenant tenant = Tenant.builder().build();
        String tenantUniqueName = "New Unique Name";
        tenant.setTenantUniqueName(tenantUniqueName);
        assertEquals(tenantUniqueName, tenant.getTenantUniqueName());
    }

    @Test
    void testSetDescriptionAndGetDescription() {
        Tenant tenant = Tenant.builder().build();
        String description = "New Description";
        tenant.setDescription(description);
        assertEquals(description, tenant.getDescription());
    }

    @Test
    void testSetColorCodeAndGetColorCode() {
        Tenant tenant = Tenant.builder().build();
        String colorCode = "#ffffff";
        tenant.setColorCode(colorCode);
        assertEquals(colorCode, tenant.getColorCode());
    }

    @Test
    void testSetActiveAndIsActive() {
        Tenant tenant = Tenant.builder().build();
        tenant.setActive(false);
        assertFalse(tenant.isActive());
        tenant.setActive(true);
        assertTrue(tenant.isActive());
    }

    @Test
    void testSetUsersAndGetUsers() {
        Tenant tenant = Tenant.builder().build();
        List<String> newUsers = Arrays.asList("user4", "user5");
        tenant.setUsers(newUsers);
        assertEquals(newUsers, tenant.getUsers());
    }

    @Test
    void testToString() {
        Tenant tenant = Tenant.builder()
                .id("id")
                .title("title")
                .tenantUniqueName("uniqueName")
                .description("description")
                .colorCode("#ffffff")
                .active(true)
                .users(Arrays.asList("user1", "user2"))
                .contactTags(new HashMap<>())
                .build();

        String expected = "Tenant(id=id, title=title, tenantUniqueName=uniqueName, description=description, colorCode=#ffffff, active=true, users=[user1, user2], contactTags={}, labels=null, displayProps=null)";
        assertEquals(expected, tenant.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Tenant tenant1 = Tenant.builder().id("id").build();
        Tenant tenant2 = Tenant.builder().id("id").build();

        assertTrue(tenant1.equals(tenant2) && tenant2.equals(tenant1));
        assertEquals(tenant1.hashCode(), tenant2.hashCode());
    }

    @Test
    void testNotEqualsAndHashCode() {
        Tenant tenant1 = Tenant.builder().id("id1").build();
        Tenant tenant2 = Tenant.builder().id("id2").build();

        assertFalse(tenant1.equals(tenant2) || tenant2.equals(tenant1));
        assertNotEquals(tenant1.hashCode(), tenant2.hashCode());
    }
}