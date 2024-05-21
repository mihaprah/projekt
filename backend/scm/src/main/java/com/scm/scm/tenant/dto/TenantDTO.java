package com.scm.scm.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO {
    private String id;
    private String title;
    private String tenantUniqueName;
    private String description;
    private String colorCode;
    private boolean active;
    private List<String> users;
    private Map<String, Integer> contactTags;
    private Map<String, String> labels;
    private List<String> displayProps;
}