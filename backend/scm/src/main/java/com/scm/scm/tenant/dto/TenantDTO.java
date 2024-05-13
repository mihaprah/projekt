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

    public TenantDTO(String id, String title, String tenantUniqueName, String description, String colorCode, boolean active, List<String> users, Map<String, Integer> contactTags) {
        this.id = id;
        this.title = title;
        this.tenantUniqueName = tenantUniqueName;
        this.description = description;
        this.colorCode = colorCode;
        this.active = active;
        this.users = users;
        this.contactTags = contactTags;
    }
}