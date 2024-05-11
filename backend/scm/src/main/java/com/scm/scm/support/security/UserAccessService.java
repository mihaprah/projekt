package com.scm.scm.support.security;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccessService {

    @Autowired
    private TenantRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(UserAccessService.class);

    public boolean hasAccessToTenant(String username, String tenantId) {
        if (username.isEmpty() || tenantId.isEmpty()) {
            logger.error("Username or TenantId is empty. Username: {}, TenantId: {}", username, tenantId);
            throw new IllegalArgumentException("Username or TenantId should not be empty");
        }
        Tenant tenant = repo.findById(tenantId).orElse(null);
        if (tenant == null) {
            logger.error("Tenant not found with id: {}", tenantId);
            throw new IllegalArgumentException("Tenant not found");
        }
        return tenant.getUsers().contains(username);
    }

    public boolean hasAccessToContact(String username, Contact contact) {
        if (username.isEmpty() || contact == null) {
            logger.error("Username is empty or Contact is null. Username: {}, Contact: {}", username, contact);
            throw new IllegalArgumentException("Username or Contact should not be null or empty");
        }
        Tenant tenant = repo.findByTenantUniqueName(contact.getTenantUniqueName());
        if (tenant == null) {
            logger.error("Tenant not found with unique name: {}", contact.getTenantUniqueName());
            throw new IllegalArgumentException("Tenant not found");
        }
        return tenant.getUsers().contains(username);
    }

}