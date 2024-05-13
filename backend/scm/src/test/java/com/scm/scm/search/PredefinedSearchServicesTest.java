package com.scm.scm.search;

import com.scm.scm.predefinedSearch.dao.PredefinedSearchRepository;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.exceptions.CustomHttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PredefinedSearchServicesTest {

    @InjectMocks
    private PredefinedSearchServices predefinedSearchServices;

    @Mock
    private PredefinedSearchRepository predefinedSearchRepository;

    private PredefinedSearch predefinedSearch;

    @BeforeEach
    public void init() throws Exception {
        try (AutoCloseable ac = MockitoAnnotations.openMocks(this)) {
            predefinedSearch = new PredefinedSearch();
        }
    }

    @Test
    public void testGetAllPredefinedSearches() {

        when(predefinedSearchRepository.findAll()).thenReturn(Collections.singletonList(predefinedSearch));

        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getAllPredefinedSearches();

        assertEquals(1, predefinedSearches.size());
        verify(predefinedSearchRepository, times(1)).findAll();
    }

    @Test
    public void testGetPredefinedSearchById() {
        predefinedSearch.setId("123");
        when(predefinedSearchRepository.findById("123")).thenReturn(java.util.Optional.of(predefinedSearch));

        PredefinedSearchDTO predefinedSearchDTO = predefinedSearchServices.getPredefinedSearchById("123");

        assertEquals("123", predefinedSearchDTO.getId());
        verify(predefinedSearchRepository, times(1)).findById("123");
    }

    @Test
    public void getAllPredefinedSearches_ReturnsListOfPredefinedSearches_WhenRepositoryHasData() {
        when(predefinedSearchRepository.findAll()).thenReturn(Arrays.asList(predefinedSearch, new PredefinedSearch()));

        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getAllPredefinedSearches();

        assertEquals(2, predefinedSearches.size());
        verify(predefinedSearchRepository, times(1)).findAll();
    }

    @Test
    public void getPredefinedSearchById_ReturnsNull_WhenIdDoesNotExist() {
        when(predefinedSearchRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.getPredefinedSearchById("123"));

        verify(predefinedSearchRepository, times(1)).findById("123");
    }

    @Test
    public void deletePredefinedSearch_ThrowsException_WhenIdDoesNotExist() {
        when(predefinedSearchRepository.existsById(anyString())).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.deletePredefinedSearch("123"));
    }

    @Test
    public void getPredefinedSearchByUser_ThrowsException_WhenUserIsEmpty() {
        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.getPredefinedSearchByUser(""));
    }

    @Test
    public void getPredefinedSearchByTenant_ThrowsException_WhenTenantIsEmpty() {
        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.getPredefinedSearchByTenant(""));
    }

}