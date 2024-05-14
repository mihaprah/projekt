package com.scm.scm.loadDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LoadAllData {

    private final LoadTenants loadTenants;

    private final LoadContacts loadContacts;

    private final LoadPredefinedSearches loadPredefinedSearches;

    @Autowired
    public LoadAllData(LoadTenants loadTenants, LoadContacts loadContacts, LoadPredefinedSearches loadPredefinedSearches) {
        this.loadTenants = loadTenants;
        this.loadContacts = loadContacts;
        this.loadPredefinedSearches = loadPredefinedSearches;
    }

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
