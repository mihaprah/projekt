package com.scm.scm.support.mongoTemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MongoTemplateServiceTests {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private MongoTemplateService mongoTemplateService;

    @Test
    public void testCreateNewTenantCollections() {
        String tenantUniqueName = "tenant";
        when(mongoTemplate.createCollection(anyString())).thenReturn(null);

        assertTrue(mongoTemplateService.createNewTenantCollections(tenantUniqueName));

        verify(mongoTemplate, times(1)).createCollection(tenantUniqueName + CollectionType.MAIN.getCollectionType());
        verify(mongoTemplate, times(1)).createCollection(tenantUniqueName + CollectionType.DELETED.getCollectionType());
        verify(mongoTemplate, times(1)).createCollection(tenantUniqueName + CollectionType.ACTIVITY.getCollectionType());
    }

    @Test
    public void testCreateNewTenantCollectionsException() {
        String tenantUniqueName = "tenant";
        doThrow(new RuntimeException()).when(mongoTemplate).createCollection(anyString());

        assertFalse(mongoTemplateService.createNewTenantCollections(tenantUniqueName));
    }

    @Test
    public void testCollectionExists() {
        String collectionName = "collection";
        when(mongoTemplate.collectionExists(collectionName)).thenReturn(true);

        assertTrue(mongoTemplateService.collectionExists(collectionName));
        verify(mongoTemplate, times(1)).collectionExists(collectionName);
    }
}