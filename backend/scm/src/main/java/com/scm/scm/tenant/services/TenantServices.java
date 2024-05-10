package com.scm.scm.tenant.services;

import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class TenantServices {

    private TenantRepository tenantRepository;
    private MongoTemplateService mongoTemplateService;
    private static final Logger log = Logger.getLogger(TenantServices.class.toString());

    public Tenant addTenant(Tenant tenant) {
        if (tenantRepository.existsById(tenant.getId())) {
            throw new CustomHttpException("Tenant with this id already exists", 400, ExceptionCause.USER_ERROR);
        } else {
            if (Objects.equals(tenant.getTitle(), "")) {
                throw new CustomHttpException("Tenant title is empty", 400, ExceptionCause.USER_ERROR);
            } else {
                tenant.setId(tenant.generateId(tenant.getTitle()));
                tenant.setTenantUniqueName(tenant.generateTenantUniqueName(tenant.getTitle()));
                if (!mongoTemplateService.createNewTenantCollections(tenant.getTenantUniqueName())) {
                    throw new CustomHttpException("Failed to create tenant collections", 500, ExceptionCause.SERVER_ERROR);
                }
                tenantRepository.save(tenant);
                log.info("Tenant created with id: " + tenant.getId());
                return tenant;
            }
        }
    }

    public List<Tenant> getAllTenants() {
        log.info("All tenants found");
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(String id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
        log.info("Tenant found with id: " + id);
        return tenant;
    }

    public Tenant updateTenant(Tenant tenant) {
        Tenant oldTenant = tenantRepository.findById(tenant.getId()).orElse(null);
        if (oldTenant != null) {
            oldTenant.setDescription(tenant.getDescription());
            oldTenant.setColorCode(tenant.getColorCode());
            oldTenant.setTitle(tenant.getTitle());
            tenantRepository.save(oldTenant);
        } else {
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
        log.info("Tenant updated with id: " + tenant.getId());
        return oldTenant;
    }

    public String deactivateTenant(String id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            tenant.setActive(false);
            tenantRepository.save(tenant);
            log.info("Tenant deactivated with id: " + id);
            return "Tenant successfully deactivated";
        } else {
            throw new CustomHttpException("Tenant is null", 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public String addTags(String tenantUniqueName, List<String> tags) {
        Tenant tenant = tenantRepository.findByTenantUniqueName(tenantUniqueName);
        if (tenant != null) {
            Map<String, Integer> oldTags = tenant.getContactTags();
            for (String tag : tags) {
                if (!oldTags.containsKey(tag)) {
                    oldTags.put(tag, 1);
                } else {
                    oldTags.put(tag, oldTags.get(tag) + 1);
                }
            }
            tenant.setContactTags(oldTags);
            tenantRepository.save(tenant);
            log.info("Tags added to tenant: " + tenantUniqueName);
            return "Tenant successfully added tags";
        } else {
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
    }

    public String removeTags(String tenantUniqueName, List<String> tags) {
        Tenant tenant = tenantRepository.findByTenantUniqueName(tenantUniqueName);
        if (tenant != null) {
            Map<String, Integer> oldTags = tenant.getContactTags();
            for (String tag : tags) {
                oldTags.put(tag, oldTags.get(tag) - 1);
                if (oldTags.get(tag) == 0) {
                    oldTags.remove(tag);
                }
            }
            tenant.setContactTags(oldTags);
            tenantRepository.save(tenant);
            log.info("Tags removed from tenant: " + tenantUniqueName);
            return "Tenant successfully removed tags";
        } else {
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
    }

    public String addUsers(String id, List<String> users) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.SERVER_ERROR));
        if (tenant != null) {
            List<String> oldUsers = tenant.getUsers();
            for (String user : users) {
                if (!oldUsers.contains(user)) {
                    oldUsers.add(user);
                }
            }
            tenant.setUsers(oldUsers);
            tenantRepository.save(tenant);
            log.info("Users added to tenant: " + id);
            return "Tenant successfully added users";
        } else {
            throw new CustomHttpException("Tenant is null", 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public String removeUsers(String id, List<String> users) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            List<String> oldUsers = tenant.getUsers();
            for (String user : users) {
                if (oldUsers.size() != 1) {
                    oldUsers.remove(user);
                }
            }
            log.info("Users removed from tenant: " + id);
            return "Tenant successfully removed users";
        } else {
            throw new CustomHttpException("Tenant is null", 500, ExceptionCause.SERVER_ERROR);
        }
    }
}
