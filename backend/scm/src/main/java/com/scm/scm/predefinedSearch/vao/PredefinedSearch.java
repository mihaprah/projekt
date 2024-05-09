package com.scm.scm.predefinedSearch.vao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Random;

@Data
@Document(collection = "predefinedSearches")
public class PredefinedSearch {

    public PredefinedSearch (String searchQuery, String searchBy, String user, String onTenant, String title, String filter, SortOrientation sortOrientation) {
        this.searchQuery = searchQuery;
        this.searchBy = searchBy;
        this.user = user;
        this.onTenant = onTenant;
        this.title = title;
        this.filter = filter;
        this.sortOrientation = sortOrientation;
    }

    @Id
    private String id;
    private String searchQuery;
    private String searchBy;
    private String user;
    private String onTenant;
    private String title;
    private String filter;
    private SortOrientation sortOrientation;

    public String generateId(String searchTitle) {
        final Random random = new Random();
        String sanitizedTitle = searchTitle.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return sanitizedTitle + "-" + System.nanoTime() + "-" + random.nextInt(10_000);
    }
}
