package com.scm.scm.support.security;

import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserAccessService {

    private final TenantRepository repo;

    private static final Logger log = Logger.getLogger(UserAccessService.class.toString());

    @Autowired
    public UserAccessService(TenantRepository repo) {
        this.repo = repo;
    }

    public boolean hasAccessToTenant(String username, String tenantId) {
        if (username.isEmpty() || tenantId.isEmpty()) {
            log.severe(String.format("Checking user access: Username or TenantId is empty. Username: %s, TenantId: %s", username, tenantId));
            throw new CustomHttpException("Username or TenantId should not be empty", 400, ExceptionCause.USER_ERROR);
        }
        Tenant tenant = repo.findById(tenantId).orElse(null);
        if (tenant == null) {
            log.severe(String.format("Checking user access: Tenant not found with id: %s", tenantId));
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
        return tenant.getUsers().contains(username);
    }

    public boolean hasAccessToContact(String username, String tenantUniqueName) {
        if (username.isEmpty() || tenantUniqueName.isEmpty()) {
            log.severe(String.format("Checking user access: Username is empty or Contact is null. Username: %s, TenantUniqueName: %s", username, tenantUniqueName));
            throw new CustomHttpException("Username or Contact should not be null or empty", 400, ExceptionCause.USER_ERROR);
        }
        Tenant tenant = repo.findByTenantUniqueName(tenantUniqueName);
        if (tenant == null) {
            log.severe(String.format("Checking user access: Tenant not found with unique name: %s", tenantUniqueName));
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
        return tenant.getUsers().contains(username);
    }

}