package com.scm.scm.search;

import com.scm.scm.predefinedSearch.vao.SortOrientation;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PredefinedSearchTests {

    private PredefinedSearch predefinedSearch;

    @BeforeEach
    void setUp() {
        predefinedSearch = new PredefinedSearch("searchQuery", "user", "onTenant", "title", Collections.singletonList("filter"), SortOrientation.ASC);
    }

    @Test
    void testConstructor() {
        assertPredefinedSearch();
    }

    @Test
    void predefinedSearchConstructorShouldSetCorrectValues() {
        assertPredefinedSearch();
    }

    private void assertPredefinedSearch() {
        assertNotNull(predefinedSearch);
        assertEquals("searchQuery", predefinedSearch.getSearchQuery());
        assertEquals("user", predefinedSearch.getUser());
        assertEquals("onTenant", predefinedSearch.getOnTenant());
        assertEquals("title", predefinedSearch.getTitle());
        assertEquals("filter", predefinedSearch.getFilter().getFirst());
        assertEquals(SortOrientation.ASC, predefinedSearch.getSortOrientation());
    }

    private void testGenerateId() {
        String title = "Test Title 123";
        String id = predefinedSearch.generateId(title);

        assertNotNull(id);
        assertTrue(id.startsWith("testtitle123-"));
        assertTrue(id.matches("testtitle123-\\d+-\\d+"));

        String anotherId = predefinedSearch.generateId(title);
        assertNotEquals(id, anotherId);
    }

    @Test
    void generateIdShouldCreateUniqueIds() {
        testGenerateId();
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

    @Test
    void testDataAnnotation() {
        PredefinedSearch search1 = new PredefinedSearch("query1", "user1", "tenant1", "title1", Collections.singletonList("filter1"), SortOrientation.ASC);
        PredefinedSearch search2 = new PredefinedSearch("query1", "user1", "tenant1", "title1", Collections.singletonList("filter1"), SortOrientation.ASC);

        assertEquals(search1, search2);
        assertEquals(search1.hashCode(), search2.hashCode());
        assertEquals(search1.toString(), search2.toString());
    }

    @Test
    void testAllArgsConstructor() {
        PredefinedSearch search = new PredefinedSearch("query", "user", "tenant", "title", Collections.singletonList("filter"), SortOrientation.ASC);

        assertEquals("query", search.getSearchQuery());
        assertEquals("user", search.getUser());
        assertEquals("tenant", search.getOnTenant());
        assertEquals("title", search.getTitle());
        assertEquals(Collections.singletonList("filter"), search.getFilter());
        assertEquals(SortOrientation.ASC, search.getSortOrientation());
    }

    @Test
    void testBuilder() {
        PredefinedSearch search = PredefinedSearch.builder()
                .searchQuery("query")
                .user("user")
                .onTenant("tenant")
                .title("title")
                .filter(Collections.singletonList("filter"))
                .sortOrientation(SortOrientation.ASC)
                .build();

        assertEquals("query", search.getSearchQuery());
        assertEquals("user", search.getUser());
        assertEquals("tenant", search.getOnTenant());
        assertEquals("title", search.getTitle());
        assertEquals(Collections.singletonList("filter"), search.getFilter());
        assertEquals(SortOrientation.ASC, search.getSortOrientation());
    }

}
