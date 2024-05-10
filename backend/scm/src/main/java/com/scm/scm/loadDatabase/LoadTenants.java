package com.scm.scm.loadDatabase;

import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import com.scm.scm.tenant.vao.Tenant;
import com.scm.scm.tenant.dao.TenantRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class LoadTenants {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private MongoTemplateService mongoTemplateService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public String[] createTenants() {

        mongoTemplate.getDb().drop();
        System.out.println("Old database dropped.");

        Tenant tenant1 = new Tenant(null, "Acme Corporation", "", "A global leader in anvil production.", "Blue", true, Arrays.asList("user1@example.com", "user2@example.com"), new HashMap<>());
        tenant1.setTenantUniqueName(tenant1.generateTenantUniqueName(tenant1.getTitle()));
        tenant1.setId(tenant1.generateId(tenant1.getTitle()));

        Tenant tenant2 = new Tenant(null, "Globex Corporation", "", "A secretive multinational corporation.", "Red", true, Arrays.asList("user3@example.com", "user4@example.com"),  new HashMap<>());
        tenant2.setTenantUniqueName(tenant2.generateTenantUniqueName(tenant2.getTitle()));
        tenant2.setId(tenant2.generateId(tenant2.getTitle()));

        Tenant savedTenant1 = tenantRepository.save(tenant1);
        Tenant savedTenant2 = tenantRepository.save(tenant2);
        System.out.println("Loaded test Tenants into the database.");

        mongoTemplateService.createNewTenantCollections(savedTenant1.getTenantUniqueName());
        mongoTemplateService.createNewTenantCollections(savedTenant2.getTenantUniqueName());
        System.out.println("Created Tenants main, deleted, activity collection in the database.");

        return new String[]{savedTenant1.getTenantUniqueName(), savedTenant2.getTenantUniqueName()};
    }
}