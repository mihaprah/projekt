package com.scm.scm.contact.vao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    private String id;
    private String title;
    private String user;
    private String tenantUniqueName;
    private String comments;
    private LocalDateTime createdAt;
    private List<String> tags;
    private Map<String, String> props;
    private String attributesToString;

    public String generateId(String contactTitle) {
        final Random random = new Random();
        String sanitizedTitle = contactTitle.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return sanitizedTitle + "-" + System.nanoTime() + "-" + random.nextInt(10_000);
    }

    public String contactAttributesToString() {
        StringBuilder contactTags = new StringBuilder();
        for (String tag : this.getTags()) {
            contactTags.append(tag.toLowerCase()).append(",");
        }
        StringBuilder contactPropsKey = new StringBuilder();
        for (String prop : this.getProps().keySet()) {
            contactPropsKey.append(prop.toLowerCase()).append(",");
        }
        return this.title.toLowerCase() + "," + contactTags + contactPropsKey;
    }

}
