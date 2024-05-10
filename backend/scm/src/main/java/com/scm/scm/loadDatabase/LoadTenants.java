package com.scm.scm.loadDatabase;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import com.scm.scm.tenant.vao.Tenant;
import com.scm.scm.tenant.dao.TenantRepository;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LoadTenants {

    @Autowired
    private TenantRepository tenantRepository;

    @Bean
    @Profile("dev")
    CommandLineRunner initDatabase() {
        return args -> {

            tenantRepository.deleteAll();

            Map<String, Integer> contactTags = new HashMap<>();
            contactTags.put("VIP", 5);
            contactTags.put("Regular", 15);

            Tenant tenant1 = new Tenant(null, "Acme Corporation", "", "A global leader in anvil production.", "Blue", true, Arrays.asList("user1@example.com", "user2@example.com"), contactTags);
            tenant1.setTenantUniqueName(tenant1.generateTenantUniqueName(tenant1.getTitle()));
            tenant1.setId(tenant1.generateId(tenant1.getTitle()));

            Tenant tenant2 = new Tenant(null, "Globex Corporation", "", "A secretive multinational corporation.", "Red", true, Arrays.asList("user3@example.com", "user4@example.com"), contactTags);
            tenant2.setTenantUniqueName(tenant2.generateTenantUniqueName(tenant2.getTitle()));
            tenant2.setId(tenant2.generateId(tenant2.getTitle()));

            tenantRepository.save(tenant1);
            tenantRepository.save(tenant2);

            System.out.println("Loaded test tenants into the database.");
        };
    }
}