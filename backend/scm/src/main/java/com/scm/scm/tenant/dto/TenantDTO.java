package com.scm.scm.tenant.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class TenantDTO {
    private String id;
    private String title;
    private String tenantUniqueName;
    private String description;
    private String colorCode;
    private boolean active;
    private List<String> users;
    private Map<String, Integer> contactTags;
}