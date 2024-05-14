package com.scm.scm.support.mongoTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MongoTemplateService {

    private static final Logger log = Logger.getLogger(MongoTemplateService.class.toString());

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoTemplateService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean createNewTenantCollections(String tenantUniqueName) {
        try {
            mongoTemplate.createCollection(tenantUniqueName + CollectionType.MAIN.getCollectionType());
            mongoTemplate.createCollection(tenantUniqueName + CollectionType.DELETED.getCollectionType());
            mongoTemplate.createCollection(tenantUniqueName + CollectionType.ACTIVITY.getCollectionType());
        } catch (Exception e) {
            return false;
        }
        log.log(Level.INFO, "Created collections for tenant: {0}", tenantUniqueName);
        return true;
    }

    public boolean collectionExists(String collectionName) {
        log.log(Level.INFO, "Checking if collection exists: {0}", collectionName);
        return mongoTemplate.collectionExists(collectionName);
    }
}
