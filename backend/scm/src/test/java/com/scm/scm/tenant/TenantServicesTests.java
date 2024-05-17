package com.scm.scm.tenant;


import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.services.TenantServices;
import com.scm.scm.tenant.vao.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class TenantServicesTests {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private MongoTemplateService mongoTemplateService;

    @InjectMocks
    private TenantServices tenantServices;

    private Tenant tenant;
    private TenantDTO tenantDTO;

    private final List<String> users = Arrays.asList("user1", "user2", "user3");

    private final Map<String, Integer> contactTags = Map.of("tag1", 1, "tag2", 2, "tag3", 3);

    @BeforeEach
    public void init() throws Exception {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
            tenant = new Tenant("id", "title", "tenantUniqueName", "Short description", "#ff4545", true, new ArrayList<>(users), new HashMap<>(contactTags), new HashMap<>(), Arrays.asList(""));
            tenantDTO = TenantDTO.builder().id("id").title("title").tenantUniqueName("tenantUniqueName").description("Short description1").colorCode("#ff4545").active(true).users(users).contactTags(contactTags).build();
        }
    }

    @Test
    void testAddTenant() {
        when(tenantRepository.existsById(anyString())).thenReturn(false);
        when(mongoTemplateService.createNewTenantCollections(anyString())).thenReturn(true);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        TenantDTO result = tenantServices.addTenant(tenantDTO);

        assertEquals(tenantDTO.getTitle(), result.getTitle());
        verify(tenantRepository, times(1)).existsById(anyString());
        verify(mongoTemplateService, times(1)).createNewTenantCollections(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testGetAllTenants() {
        List<Tenant> tenants = Collections.singletonList(tenant);

        when(tenantRepository.findAll()).thenReturn(tenants);

        List<TenantDTO> result = tenantServices.getAllTenants();

        assertEquals(1, result.size());
        assertEquals(tenant.getTitle(), result.getFirst().getTitle());
        verify(tenantRepository, times(1)).findAll();
    }

    @Test
    void testGetTenantById() {

        when(tenantRepository.findById(anyString())).thenReturn(Optional.of(tenant));

        TenantDTO result = tenantServices.getTenantById("1");

        assertEquals(tenant.getTitle(), result.getTitle());
        verify(tenantRepository, times(1)).findById(anyString());
    }

    @Test
    void testUpdateTenant() {
        tenantDTO.setTitle("Updated Tenant");

        when(tenantRepository.findById(anyString())).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        TenantDTO result = tenantServices.updateTenant(tenantDTO);

        assertEquals(tenantDTO.getTitle(), result.getTitle());
        verify(tenantRepository, times(1)).findById(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testDeactivateTenant() {
        when(tenantRepository.findById(anyString())).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        String result = tenantServices.deactivateTenant("1");

        assertEquals("Tenant successfully deactivated", result);
        verify(tenantRepository, times(1)).findById(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testGetTenantsByUser() {
        when(tenantRepository.findByUsersContaining(anyString())).thenReturn(Collections.singletonList(tenant));

        List<TenantDTO> result = tenantServices.getTenantsByUser("user1");

        assertEquals(1, result.size());
        assertEquals(tenant.getTitle(), result.getFirst().getTitle());
        verify(tenantRepository, times(1)).findByUsersContaining(anyString());
    }

    @Test
    void testAddTags() {
        when(tenantRepository.findByTenantUniqueName(anyString())).thenReturn(tenant);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        String result = tenantServices.addTags(tenant.getTenantUniqueName(), Arrays.asList("tag4", "tag5"));

        assertEquals("Tenant successfully added tags", result);
        verify(tenantRepository, times(1)).findByTenantUniqueName(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testRemoveTags() {
        when(tenantRepository.findByTenantUniqueName(anyString())).thenReturn(tenant);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        String result = tenantServices.removeTags(tenant.getTenantUniqueName(), Arrays.asList("tag1", "tag2"));

        assertEquals("Tenant successfully removed tags", result);
        verify(tenantRepository, times(1)).findByTenantUniqueName(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testAddUsers() {
        when(tenantRepository.findById(anyString())).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        String result = tenantServices.addUsers(tenant.getId(), Arrays.asList("user4", "user5"));

        assertEquals("Users added to Tenant successfully", result);
        verify(tenantRepository, times(1)).findById(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testRemoveUsers() {
        when(tenantRepository.findById(anyString())).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        String result = tenantServices.removeUsers(tenant.getId(), Arrays.asList("user1", "user2"));

        assertEquals("Users removed from Tenant successfully", result);
        verify(tenantRepository, times(1)).findById(anyString());
        verify(tenantRepository, times(1)).save(any(Tenant.class));
    }

    @Test
    void testAddTenantWithExistingId() {
        when(tenantRepository.existsById(anyString())).thenReturn(true);

        assertThrows(CustomHttpException.class, () -> tenantServices.addTenant(tenantDTO));
        verify(tenantRepository, times(1)).existsById(anyString());
    }

    @Test
    void testAddTenantWithEmptyTitle() {
        tenantDTO.setTitle("");
        assertThrows(CustomHttpException.class, () -> tenantServices.addTenant(tenantDTO));
    }

    @Test
    void testAddTenantWithFailedCollectionCreation() {
        when(tenantRepository.existsById(anyString())).thenReturn(false);
        when(mongoTemplateService.createNewTenantCollections(anyString())).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> tenantServices.addTenant(tenantDTO));
        verify(tenantRepository, times(1)).existsById(anyString());
        verify(mongoTemplateService, times(1)).createNewTenantCollections(anyString());
    }

    @Test
    void testGetTenantByIdNotFound() {
        when(tenantRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> tenantServices.getTenantById("1"));
        verify(tenantRepository, times(1)).findById(anyString());
    }

    @Test
    void testUpdateTenantNotFound() {
        when(tenantRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> tenantServices.updateTenant(tenantDTO));
        verify(tenantRepository, times(1)).findById(anyString());
    }

    @Test
    void testDeactivateTenantNotFound() {
        when(tenantRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> tenantServices.deactivateTenant("1"));
        verify(tenantRepository, times(1)).findById(anyString());
    }

    @Test
    void testAddTagsNotFound() {
        String tenantUniqueName = tenant.getTenantUniqueName();
        when(tenantRepository.findByTenantUniqueName(tenantUniqueName)).thenReturn(null);

        assertThrows(CustomHttpException.class, () -> tenantServices.addTags(tenantUniqueName, Arrays.asList("tag4", "tag5")));
        verify(tenantRepository, times(1)).findByTenantUniqueName(tenantUniqueName);
    }

    @Test
    void testRemoveTagsNotFound() {
        String tenantUniqueName = tenant.getTenantUniqueName();
        when(tenantRepository.findByTenantUniqueName(tenantUniqueName)).thenReturn(null);

        assertThrows(CustomHttpException.class, () -> tenantServices.removeTags(tenantUniqueName, Arrays.asList("tag1", "tag2")));
        verify(tenantRepository, times(1)).findByTenantUniqueName(tenantUniqueName);
    }


    @Test
    void testAddUsersNotFound() {
        String tenantId = tenant.getId();
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> tenantServices.addUsers(tenantId, Arrays.asList("user4", "user5")));
        verify(tenantRepository, times(1)).findById(tenantId);
    }

    @Test
    void testRemoveUsersNotFound() {
        String tenantId = tenant.getId();
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> tenantServices.removeUsers(tenantId, Arrays.asList("user1", "user2")));
        verify(tenantRepository, times(1)).findById(tenantId);
    }

    @Test
    void testRemoveUsersLastUser() {
        String tenantId = tenant.getId();
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));

        assertThrows(CustomHttpException.class, () -> tenantServices.removeUsers(tenantId, users));
        verify(tenantRepository, times(1)).findById(tenantId);
    }
}
