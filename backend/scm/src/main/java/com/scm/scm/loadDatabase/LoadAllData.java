package com.scm.scm.loadDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LoadAllData {

    @Autowired
    private LoadTenants loadTenants;

    @Autowired
    private LoadContacts loadContacts;

    @Autowired
    private LoadPredefinedSearches loadPredefinedSearches;

    @Bean
    @Profile("dev")
    CommandLineRunner initDatabase() {
        return args -> {
            String[] tenantUniqueNames = loadTenants.createTenants();
            loadContacts.createContacts(tenantUniqueNames);
            loadPredefinedSearches.createPredefinedSearches();
        };
    }
}
