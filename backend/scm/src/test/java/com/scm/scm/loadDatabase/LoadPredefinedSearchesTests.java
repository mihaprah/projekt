package com.scm.scm.loadDatabase;

import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;

class LoadPredefinedSearchesTest {

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    LoadPredefinedSearches loadPredefinedSearches;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPredefinedSearches() {
        loadPredefinedSearches.createPredefinedSearches();

        verify(mongoTemplate, times(1)).save(any(PredefinedSearch.class));
    }
}