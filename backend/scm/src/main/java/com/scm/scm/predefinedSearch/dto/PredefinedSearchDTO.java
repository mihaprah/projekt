package com.scm.scm.predefinedSearch.dto;

import com.scm.scm.predefinedSearch.vao.SortOrientation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredefinedSearchDTO {
    private String id;
    private String searchQuery;
    private String user;
    private String onTenant;
    private String title;
    private List<String> filter;
    private SortOrientation sortOrientation;
}
