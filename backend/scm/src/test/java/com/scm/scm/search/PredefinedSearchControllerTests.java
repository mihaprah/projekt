/**package com.scm.scm.search;

import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PredefinedSearchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PredefinedSearchServices predefinedSearchServices;

    private PredefinedSearchDTO predefinedSearchDTO;

    @BeforeEach
    void setup() {
        predefinedSearchDTO = new PredefinedSearchDTO();
        predefinedSearchDTO.setId("123");
        predefinedSearchDTO.setTitle("Test Title");
    }

    @Test
    void testGetPredefinedSearches() throws Exception {
        when(predefinedSearchServices.getAllPredefinedSearches()).thenReturn(Collections.singletonList(predefinedSearchDTO));

        mockMvc.perform(get("/predefined_searches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPredefinedSearch() throws Exception {
        when(predefinedSearchServices.getPredefinedSearchById("123")).thenReturn(predefinedSearchDTO);

        mockMvc.perform(get("/predefined_searches/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreatePredefinedSearch() throws Exception {
        when(predefinedSearchServices.addPredefinedSearch(predefinedSearchDTO)).thenReturn(predefinedSearchDTO);

        mockMvc.perform(post("/predefined_searches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"123\",\"title\":\"Test Title\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePredefinedSearch() throws Exception {
        when(predefinedSearchServices.updatePredefinedSearch(predefinedSearchDTO)).thenReturn(predefinedSearchDTO);

        mockMvc.perform(put("/predefined_searches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"123\",\"title\":\"Test Title\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePredefinedSearch() throws Exception {
        when(predefinedSearchServices.deletePredefinedSearch("123")).thenReturn("PredefinedSearch successfully deleted");

        mockMvc.perform(delete("/predefined_searches/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPredefinedSearchByUser() throws Exception {
        when(predefinedSearchServices.getPredefinedSearchByUser("testUser")).thenReturn(Collections.singletonList(predefinedSearchDTO));

        mockMvc.perform(get("/predefined_searches/user/testUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPredefinedSearchByTenant() throws Exception {
        when(predefinedSearchServices.getPredefinedSearchByTenant("testTenant")).thenReturn(Collections.singletonList(predefinedSearchDTO));

        mockMvc.perform(get("/predefined_searches/tenant/testTenant")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}*/