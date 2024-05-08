package com.scm.scm.predefinedSearches.vao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "predefinedSearches")
public class PredefinedSearch {

    public PredefinedSearch (String searchQuery, String user, String onTenant) {
        this.searchQuery = searchQuery;
        this.user = user;
        this.onTenant = onTenant;
    }

    public enum SortOrientation { ASC, DESC }

    @Id
    private String id;
    private String searchQuery;
    private String searchBy;
    private String user;
    private String onTenant;
    private String title;
    private String filter;

}
