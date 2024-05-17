package com.scm.scm.tenant.vao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "all-tenants")
public class Tenant {
    @Id
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

    private static final SecureRandom random = new SecureRandom();

    public String generateTenantUniqueName(String tenantTitle) {
        LocalDate date = LocalDate.now();
        if (tenantTitle.length() >= 3) {
            String sanitizedTitle = tenantTitle.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

            String initials = sanitizedTitle.substring(0, Math.min(sanitizedTitle.length(), 3));

            String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM-yy"));

            return initials + "-" + formattedDate + "-" + random.nextInt(1_000);
        } else {
            throw new IllegalArgumentException("Tenant title must contain at least 3 characters");
        }
    }

    public String generateId(String tenantTitle) {
        String sanitizedTitle = tenantTitle.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return sanitizedTitle + "-" + System.nanoTime() + "-" + random.nextInt(10_000);
    }
}

