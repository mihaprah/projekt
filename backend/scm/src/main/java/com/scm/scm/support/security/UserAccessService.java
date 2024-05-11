package com.scm.scm.support.security;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserAccessService {

    @Autowired
    private TenantRepository repo;

    private static final Logger log = Logger.getLogger(UserAccessService.class.toString());

    public boolean hasAccessToTenant(String username, String tenantId) {
        if (username.isEmpty() || tenantId.isEmpty()) {
            log.severe("Checking user access: Username or TenantId is empty. Username: " + username + ", TenantId: " + tenantId);
            throw new IllegalArgumentException("Username or TenantId should not be empty");
        }
        Tenant tenant = repo.findById(tenantId).orElse(null);
        if (tenant == null) {
            log.severe("Checking user access: Tenant not found with id: " + tenantId);
            throw new IllegalArgumentException("Tenant not found");
        }
        return tenant.getUsers().contains(username);
    }

    public boolean hasAccessToContact(String username, Contact contact) {
        if (username.isEmpty() || contact == null) {
            log.severe("Checking user access: Username is empty or Contact is null. Username: " + username + ", Contact: " + contact);
            throw new IllegalArgumentException("Username or Contact should not be null or empty");
        }
        Tenant tenant = repo.findByTenantUniqueName(contact.getTenantUniqueName());
        if (tenant == null) {
            log.severe("Checking user access: Tenant not found with unique name: " + contact.getTenantUniqueName());
            throw new IllegalArgumentException("Tenant not found");
        }
        return tenant.getUsers().contains(username);
    }

}