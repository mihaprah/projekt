package com.scm.scm.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String tags;
    private String props;
    private String attributesToString;
}
