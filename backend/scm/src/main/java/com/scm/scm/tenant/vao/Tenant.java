package com.scm.scm.tenant.vao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public String generateTenantUniqueName(String tenantTitle) {
        LocalDate date = LocalDate.now();
        final Random random = new Random();
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
        final Random random = new Random();
        String sanitizedTitle = tenantTitle.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return sanitizedTitle + "-" + System.nanoTime() + "-" + random.nextInt(10_000);
    }
}

