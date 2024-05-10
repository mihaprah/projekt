package com.scm.scm.support.mongoTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class MongoTemplateService {

    private static final Logger log = Logger.getLogger(MongoTemplateService.class.toString());

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean createNewTenantCollections(String tenantUniqueName) {
        try {
            mongoTemplate.createCollection(tenantUniqueName + "_main");
            mongoTemplate.createCollection(tenantUniqueName + "_deleted");
            mongoTemplate.createCollection(tenantUniqueName + "_activity");
        } catch (Exception e) {
            return false;
        }
        log.info("Created collections for tenant: " + tenantUniqueName);
        return true;
    }

    public boolean collectionExists(String collectionName) {
        log.info("Checking if collection exists: " + collectionName);
        return mongoTemplate.collectionExists(collectionName);
    }
}
