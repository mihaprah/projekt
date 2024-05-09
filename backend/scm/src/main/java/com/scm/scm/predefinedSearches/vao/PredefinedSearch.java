package com.scm.scm.predefinedSearches.vao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Random;

@Data
@Document(collection = "predefinedSearches")
public class PredefinedSearch {

    public PredefinedSearch (String searchQuery, String user, String onTenant) {
        this.searchQuery = searchQuery;
        this.user = user;
        this.onTenant = onTenant;
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
