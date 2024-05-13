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

    @Autowired
    private TenantRepository repo;

    private static final Logger log = Logger.getLogger(UserAccessService.class.toString());

    public boolean hasAccessToTenant(String username, String tenantId) {
        if (username.isEmpty() || tenantId.isEmpty()) {
            log.severe("Checking user access: Username or TenantId is empty. Username: " + username + ", TenantId: " + tenantId);
            throw new CustomHttpException("Username or TenantId should not be empty", 400, ExceptionCause.USER_ERROR);
        }
        Tenant tenant = repo.findById(tenantId).orElse(null);
        if (tenant == null) {
            log.severe("Checking user access: Tenant not found with id: " + tenantId);
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
        return tenant.getUsers().contains(username);
    }

    public boolean hasAccessToContact(String username, String tenantUniqueName) {
        if (username.isEmpty() || tenantUniqueName.isEmpty()) {
            log.severe("Checking user access: Username is empty or Contact is null. Username: " + username + ", TenantUniqueName: " + tenantUniqueName);
            throw new CustomHttpException("Username or Contact should not be null or empty", 400, ExceptionCause.USER_ERROR);
        }
        Tenant tenant = repo.findByTenantUniqueName(tenantUniqueName);
        if (tenant == null) {
            log.severe("Checking user access: Tenant not found with unique name: " + tenantUniqueName);
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
        return tenant.getUsers().contains(username);
    }

}