package com.scm.scm.loadDatabase;

import com.mongodb.client.MongoDatabase;
import com.scm.scm.tenant.vao.Tenant;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;

class LoadTenantsTest {

    @Mock
    TenantRepository tenantRepository;

    @Mock
    MongoTemplateService mongoTemplateService;

    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    MongoDatabase mongoDatabase;

    @InjectMocks
    LoadTenants loadTenants;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTenants() {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(i -> i.getArgument(0));

        loadTenants.createTenants();

        verify(mongoDatabase, times(1)).drop();
        verify(tenantRepository, times(2)).save(any(Tenant.class));
        verify(mongoTemplateService, times(2)).createNewTenantCollections(anyString());
    }
}