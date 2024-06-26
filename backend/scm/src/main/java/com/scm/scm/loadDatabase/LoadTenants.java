package com.scm.scm.loadDatabase;

import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import com.scm.scm.tenant.vao.Tenant;
import com.scm.scm.tenant.dao.TenantRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

@Service
public class LoadTenants {

    private static final Logger log = Logger.getLogger(LoadTenants.class.toString());

    private final TenantRepository tenantRepository;

    private final MongoTemplateService mongoTemplateService;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public LoadTenants(TenantRepository tenantRepository, MongoTemplateService mongoTemplateService, MongoTemplate mongoTemplate) {
        this.tenantRepository = tenantRepository;
        this.mongoTemplateService = mongoTemplateService;
        this.mongoTemplate = mongoTemplate;
    }

    public String[] createTenants() {

        mongoTemplate.getDb().drop();
        log.info("Old database dropped.");

        Tenant tenant1 = new Tenant(null, "Acme Corporation", "", "A global leader in anvil production.", "Blue", true, Arrays.asList("user1@example.com", "user2@example.com"), new HashMap<>(), new HashMap<>(), Arrays.asList(""));
        tenant1.setTenantUniqueName(tenant1.generateTenantUniqueName(tenant1.getTitle()));
        tenant1.setId(tenant1.generateId(tenant1.getTitle()));

        Tenant tenant2 = new Tenant(null, "Globex Corporation", "", "A secretive multinational corporation.", "Red", true, Arrays.asList("user3@example.com", "user4@example.com"),  new HashMap<>(), new HashMap<>(),  Arrays.asList(""));
        tenant2.setTenantUniqueName(tenant2.generateTenantUniqueName(tenant2.getTitle()));
        tenant2.setId(tenant2.generateId(tenant2.getTitle()));

        Tenant savedTenant1 = tenantRepository.save(tenant1);
        Tenant savedTenant2 = tenantRepository.save(tenant2);
        log.info("Loaded test Tenants into the database.");

        mongoTemplateService.createNewTenantCollections(savedTenant1.getTenantUniqueName());
        mongoTemplateService.createNewTenantCollections(savedTenant2.getTenantUniqueName());
        log.info("Created Tenants main, deleted, activity collection in the database.");

        return new String[]{savedTenant1.getTenantUniqueName(), savedTenant2.getTenantUniqueName()};
    }
}