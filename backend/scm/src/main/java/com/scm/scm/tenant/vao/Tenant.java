package com.scm.scm.tenant.vao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Document(collection = "all-tenants")
public class Tenant {
    @Id
    private String id;
    private String title;
    private String tenantUniqueName;
    private String description;
    private String colorCode;
    private boolean active = true;
    private List<String> users;
    private Map<String, Integer> contactTags;
}

