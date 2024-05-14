package com.scm.scm.search;

import com.scm.scm.predefinedSearch.vao.SortOrientation;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PredefinedSearchTests {

    private PredefinedSearch predefinedSearch;

    @BeforeEach
    void setUp() {
        predefinedSearch = new PredefinedSearch("searchQuery", "user", "onTenant", "title", Collections.singletonList("filter"), SortOrientation.ASC);
    }

    @Test
    void testConstructor() {
        assertNotNull(predefinedSearch);
        assertEquals("searchQuery", predefinedSearch.getSearchQuery());
        assertEquals("user", predefinedSearch.getUser());
        assertEquals("onTenant", predefinedSearch.getOnTenant());
        assertEquals("title", predefinedSearch.getTitle());
        assertEquals("filter", predefinedSearch.getFilter().getFirst());
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

    @Test
    void predefinedSearchConstructorShouldSetCorrectValues() {

        assertNotNull(predefinedSearch);
        assertEquals("searchQuery", predefinedSearch.getSearchQuery());
        assertEquals("user", predefinedSearch.getUser());
        assertEquals("onTenant", predefinedSearch.getOnTenant());
        assertEquals("title", predefinedSearch.getTitle());
        assertEquals("filter", predefinedSearch.getFilter());
        assertEquals(SortOrientation.ASC, predefinedSearch.getSortOrientation());
    }

    @Test
    void generateIdShouldCreateUniqueIds() {
        String title = "Test Title 123";
        String id = predefinedSearch.generateId(title);

        assertNotNull(id);
        assertTrue(id.startsWith("testtitle123-"));
        assertTrue(id.matches("testtitle123-\\d+-\\d+"));

        String anotherId = predefinedSearch.generateId(title);
        assertNotEquals(id, anotherId);
    }

    @Test
    void generateIdShouldHandleEmptyTitle() {
        String title = "";
        String id = predefinedSearch.generateId(title);

        assertNotNull(id);
        assertTrue(id.startsWith("-empty-"));
        assertTrue(id.matches("-empty-\\d+-\\d+"));
    }

    @Test
    void generateIdShouldHandleNullTitle() {
        String id = predefinedSearch.generateId(null);

        assertNotNull(id);
        assertTrue(id.startsWith("-empty-"));
        assertTrue(id.matches("-empty-\\d+-\\d+"));
    }

    @Test
    void generateIdShouldRemoveSpecialCharactersFromTitle() {
        String title = "Test! Title@ 123#";
        String id = predefinedSearch.generateId(title);

        assertNotNull(id);
        assertTrue(id.startsWith("testtitle123-"));
        assertTrue(id.matches("testtitle123-\\d+-\\d+"));
    }

}
