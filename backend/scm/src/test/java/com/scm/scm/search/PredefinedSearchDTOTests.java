package com.scm.scm.search;

import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.vao.SortOrientation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class PredefinedSearchDTOTests {

    @Test
    public void testNoArgsConstructor() {
        PredefinedSearchDTO predefinedSearchDTO = new PredefinedSearchDTO();

        assertNull(predefinedSearchDTO.getId());
        assertNull(predefinedSearchDTO.getSearchQuery());
        assertNull(predefinedSearchDTO.getUser());
        assertNull(predefinedSearchDTO.getOnTenant());
        assertNull(predefinedSearchDTO.getTitle());
        assertNull(predefinedSearchDTO.getFilter());
        assertNull(predefinedSearchDTO.getSortOrientation());
    }

    @Test
    public void testAllArgsConstructor() {
        List<String> filter = Collections.singletonList("Test Filter");

        PredefinedSearchDTO predefinedSearchDTO = new PredefinedSearchDTO("123", "Test Query", "Test User", "Test Tenant", "Test Title", filter, SortOrientation.ASC);

        assertEquals("123", predefinedSearchDTO.getId());
        assertEquals("Test Query", predefinedSearchDTO.getSearchQuery());
        assertEquals("Test User", predefinedSearchDTO.getUser());
        assertEquals("Test Tenant", predefinedSearchDTO.getOnTenant());
        assertEquals("Test Title", predefinedSearchDTO.getTitle());
        assertEquals(filter, predefinedSearchDTO.getFilter());
        assertEquals(SortOrientation.ASC, predefinedSearchDTO.getSortOrientation());
    }

    @Test
    public void testBuilder() {
        List<String> filter = Collections.singletonList("Test Filter");

        PredefinedSearchDTO predefinedSearchDTO = PredefinedSearchDTO.builder()
                .id("123")
                .searchQuery("Test Query")
                .user("Test User")
                .onTenant("Test Tenant")
                .title("Test Title")
                .filter(filter)
                .sortOrientation(SortOrientation.ASC)
                .build();

        assertEquals("123", predefinedSearchDTO.getId());
        assertEquals("Test Query", predefinedSearchDTO.getSearchQuery());
        assertEquals("Test User", predefinedSearchDTO.getUser());
        assertEquals("Test Tenant", predefinedSearchDTO.getOnTenant());
        assertEquals("Test Title", predefinedSearchDTO.getTitle());
        assertEquals(filter, predefinedSearchDTO.getFilter());
        assertEquals(SortOrientation.ASC, predefinedSearchDTO.getSortOrientation());
    }
}