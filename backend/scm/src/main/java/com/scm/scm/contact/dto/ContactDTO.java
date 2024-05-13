package com.scm.scm.contact.dto;

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
public class ContactDTO {
    private String id;
    private String title;
    private String user;
    private String tenantUniqueName;
    private String comments;
    private String createdAt;
    private List<String> tags;
    private Map<String, String> props;
    private String attributesToString;
}
