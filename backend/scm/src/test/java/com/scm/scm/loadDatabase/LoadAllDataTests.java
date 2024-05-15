package com.scm.scm.loadDatabase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import static org.mockito.Mockito.*;

class LoadAllDataTest {

    @Mock
    LoadTenants loadTenants;

    @Mock
    LoadContacts loadContacts;

    @Mock
    LoadPredefinedSearches loadPredefinedSearches;

    @InjectMocks
    LoadAllData loadAllData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initDatabase() throws Exception {
        String[] tenantUniqueNames = {"tenant1", "tenant2"};
        when(loadTenants.createTenants()).thenReturn(tenantUniqueNames);

        CommandLineRunner runner = loadAllData.initDatabase();
        runner.run();

        verify(loadTenants, times(1)).createTenants();
        verify(loadContacts, times(1)).createContacts(tenantUniqueNames);
        verify(loadPredefinedSearches, times(1)).createPredefinedSearches();
    }

    @Test
    void initDatabase_noTenants() throws Exception {
        String[] tenantUniqueNames = {};
        when(loadTenants.createTenants()).thenReturn(tenantUniqueNames);

        CommandLineRunner runner = loadAllData.initDatabase();
        runner.run();

        verify(loadTenants, times(1)).createTenants();
        verify(loadContacts, times(1)).createContacts(tenantUniqueNames);
        verify(loadPredefinedSearches, times(1)).createPredefinedSearches();
    }
}