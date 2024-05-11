package com.scm.scm.support.security;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccessService {

    @Autowired
    private TenantRepository repo;

    public boolean hasAccessToTenant(String username, String tenantId) {
        if (username.isEmpty() || tenantId.isEmpty()) {
            return false;
        }
        Tenant tenant = repo.findById(tenantId).orElse(null);
        if (tenant == null) {
            return false;
        }
        return tenant.getUsers().contains(username);
    }

    public boolean hasAccessToContact(String username, Contact contact) {
        if (username.isEmpty() || contact == null) {
            return false;
        }
        Tenant tenant = repo.findByTenantUniqueName(contact.getTenantUniqueName());
        if (tenant == null) {
            return false;
        }
        return tenant.getUsers().contains(username);
    }

}
