package com.scm.scm.contact.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDTO {
    private String id;
    private String title;
    private String user;
    private String tenantUniqueName;
    private String comments;
    private String createdAt;
    private String tags;
    private String props;
    private String attributesToString;
}
