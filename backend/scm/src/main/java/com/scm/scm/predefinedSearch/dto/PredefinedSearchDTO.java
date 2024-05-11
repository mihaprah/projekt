package com.scm.scm.predefinedSearch.dto;

import com.scm.scm.predefinedSearch.vao.SortOrientation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredefinedSearchDTO {
    private String id;
    private String searchQuery;
    private String user;
    private String onTenant;
    private String title;
    private String filter;
    private SortOrientation sortOrientation;
}
