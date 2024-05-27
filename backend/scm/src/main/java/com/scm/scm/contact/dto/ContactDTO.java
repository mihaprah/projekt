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

    public String contactAttributesToString() {
        StringBuilder contactTags = new StringBuilder();
        for (String tag : this.getTags()) {
            contactTags.append(tag.toLowerCase()).append(",");
        }
        StringBuilder contactPropsKey = new StringBuilder();
        for (String prop : this.getProps().keySet()) {
            contactPropsKey.append(prop.toLowerCase()).append(",");
        }
        StringBuilder contactPropsValues = new StringBuilder();
        for (String prop : this.getProps().values()) {
            contactPropsValues.append(prop.toLowerCase()).append(",");
        }
        return this.title.toLowerCase() + "," + contactTags + contactPropsKey + contactPropsValues;
    }
}
