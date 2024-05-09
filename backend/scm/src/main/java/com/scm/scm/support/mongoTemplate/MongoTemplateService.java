package com.scm.scm.support.mongoTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoTemplateService {

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
        return true;
    }

    public boolean collectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }
}
