package com.scm.scm.search;

import com.scm.scm.predefinedSearch.vao.SortOrientation;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PredefinedSearchTests {

    private PredefinedSearch predefinedSearch;

    @BeforeEach
    void setUp() {
        predefinedSearch = new PredefinedSearch("searchQuery", "searchBy", "user", "onTenant", "title", "filter", SortOrientation.ASC);
    }

    @Test
    void testConstructor() {
        assertNotNull(predefinedSearch);
        assertEquals("searchQuery", predefinedSearch.getSearchQuery());
        assertEquals("searchBy", predefinedSearch.getSearchBy());
        assertEquals("user", predefinedSearch.getUser());
        assertEquals("onTenant", predefinedSearch.getOnTenant());
        assertEquals("title", predefinedSearch.getTitle());
        assertEquals("filter", predefinedSearch.getFilter());
        assertEquals(SortOrientation.ASC, predefinedSearch.getSortOrientation());
    }

    @Test
    void testGenerateId() {
        String title = "Test Title 123";
        String id = predefinedSearch.generateId(title);

        assertNotNull(id);
        assertTrue(id.startsWith("testtitle123-"));
        assertTrue(id.matches("testtitle123-\\d+-\\d+"));

        String anotherId = predefinedSearch.generateId(title);
        assertNotEquals(id, anotherId);
    }

}
