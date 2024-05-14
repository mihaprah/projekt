package com.scm.scm.search;

import com.scm.scm.predefinedSearch.dao.PredefinedSearchRepository;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.predefinedSearch.vao.SortOrientation;
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
class PredefinedSearchServicesTest {

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
    void testGetAllPredefinedSearches() {

        when(predefinedSearchRepository.findAll()).thenReturn(Collections.singletonList(predefinedSearch));

        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getAllPredefinedSearches();

        assertEquals(1, predefinedSearches.size());
        verify(predefinedSearchRepository, times(1)).findAll();
    }

    @Test
    void testGetPredefinedSearchById() {
        predefinedSearch.setId("123");
        when(predefinedSearchRepository.findById("123")).thenReturn(java.util.Optional.of(predefinedSearch));

        PredefinedSearchDTO predefinedSearchDTO = predefinedSearchServices.getPredefinedSearchById("123");

        assertEquals("123", predefinedSearchDTO.getId());
        verify(predefinedSearchRepository, times(1)).findById("123");
    }

    @Test
    void getAllPredefinedSearches_ReturnsListOfPredefinedSearches_WhenRepositoryHasData() {
        when(predefinedSearchRepository.findAll()).thenReturn(Arrays.asList(predefinedSearch, new PredefinedSearch()));

        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getAllPredefinedSearches();

        assertEquals(2, predefinedSearches.size());
        verify(predefinedSearchRepository, times(1)).findAll();
    }

    @Test
    void getPredefinedSearchById_ReturnsNull_WhenIdDoesNotExist() {
        when(predefinedSearchRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.getPredefinedSearchById("123"));

        verify(predefinedSearchRepository, times(1)).findById("123");
    }

    @Test
    void deletePredefinedSearch_ThrowsException_WhenIdDoesNotExist() {
        when(predefinedSearchRepository.existsById(anyString())).thenReturn(false);

        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.deletePredefinedSearch("123"));
    }

    @Test
    void getPredefinedSearchByUser_ThrowsException_WhenUserIsEmpty() {
        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.getPredefinedSearchByUser(""));
    }

    @Test
    void getPredefinedSearchByTenant_ThrowsException_WhenTenantIsEmpty() {
        assertThrows(CustomHttpException.class, () -> predefinedSearchServices.getPredefinedSearchByTenant(""));
    }

    @Test
    void testAddPredefinedSearch() {
        PredefinedSearchDTO predefinedSearchDTO = new PredefinedSearchDTO();
        predefinedSearchDTO.setTitle("Test Title");

        when(predefinedSearchRepository.save(any(PredefinedSearch.class))).thenReturn(predefinedSearch);

        PredefinedSearchDTO result = predefinedSearchServices.addPredefinedSearch(predefinedSearchDTO);

        assertEquals("Test Title", result.getTitle());
        verify(predefinedSearchRepository, times(1)).save(any(PredefinedSearch.class));
    }

    @Test
    void testUpdatePredefinedSearch() {
        PredefinedSearchDTO predefinedSearchDTO = new PredefinedSearchDTO();
        predefinedSearchDTO.setId("123");
        predefinedSearchDTO.setTitle("Updated Title");

        when(predefinedSearchRepository.findById("123")).thenReturn(Optional.of(predefinedSearch));
        when(predefinedSearchRepository.save(any(PredefinedSearch.class))).thenReturn(predefinedSearch);

        PredefinedSearchDTO result = predefinedSearchServices.updatePredefinedSearch(predefinedSearchDTO);

        assertEquals("Updated Title", result.getTitle());
        verify(predefinedSearchRepository, times(1)).findById("123");
        verify(predefinedSearchRepository, times(1)).save(any(PredefinedSearch.class));
    }

    @Test
    void testDeletePredefinedSearch() {
        when(predefinedSearchRepository.existsById("123")).thenReturn(true);

        predefinedSearchServices.deletePredefinedSearch("123");

        verify(predefinedSearchRepository, times(1)).deleteById("123");
    }

    @Test
    void testGetPredefinedSearchByUser() {
        when(predefinedSearchRepository.findByUser("testUser")).thenReturn(Collections.singletonList(predefinedSearch));

        List<PredefinedSearchDTO> result = predefinedSearchServices.getPredefinedSearchByUser("testUser");

        assertEquals(1, result.size());
        verify(predefinedSearchRepository, times(1)).findByUser("testUser");
    }

    @Test
    void testGetPredefinedSearchByTenant() {
        when(predefinedSearchRepository.findByOnTenant("testTenant")).thenReturn(Collections.singletonList(predefinedSearch));

        List<PredefinedSearchDTO> result = predefinedSearchServices.getPredefinedSearchByTenant("testTenant");

        assertEquals(1, result.size());
        verify(predefinedSearchRepository, times(1)).findByOnTenant("testTenant");
    }

    @Test
    void testConvertToEntity() {
        PredefinedSearchDTO predefinedSearchDTO = new PredefinedSearchDTO();
        predefinedSearchDTO.setId("123");
        predefinedSearchDTO.setSearchQuery("Test Query");
        predefinedSearchDTO.setUser("Test User");
        predefinedSearchDTO.setOnTenant("Test Tenant");
        predefinedSearchDTO.setTitle("Test Title");
        predefinedSearchDTO.setFilter(Collections.singletonList("Test Filter"));
        predefinedSearchDTO.setSortOrientation(SortOrientation.ASC);

        PredefinedSearch result = predefinedSearchServices.convertToEntity(predefinedSearchDTO);

        assertEquals("123", result.getId());
        assertEquals("Test Query", result.getSearchQuery());
        assertEquals("Test User", result.getUser());
        assertEquals("Test Tenant", result.getOnTenant());
        assertEquals("Test Title", result.getTitle());
        assertEquals(Collections.singletonList("Test Filter"), result.getFilter());
        assertEquals(SortOrientation.ASC, result.getSortOrientation());
    }

}