package com.scm.scm.tenant.services;

import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.vao.Tenant;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TenantServices {

    private TenantRepository tenantRepository;

    public Tenant addTenant(Tenant tenant) {
        if (tenantRepository.existsById(tenant.getId())) {
            throw new CustomHttpException("Tenant with this id already exists", 400, ExceptionCause.USER_ERROR);
        } else {
            if (Objects.equals(tenant.getTitle(), "")) {
                throw new CustomHttpException("Tenant title is empty", 400, ExceptionCause.USER_ERROR);
            } else {
                tenant.setId(tenant.generateId(tenant.getTitle()));
                tenant.setTenantUniqueName(tenant.generateTenantUniqueName(tenant.getTitle()));
                tenantRepository.save(tenant);
                return tenant;
            }
        }
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(String id) {
        return tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
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
        return oldTenant;
    }

    public ResponseEntity<String> deactivateTenant(String id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));

        if (tenant != null) {
            tenant.setActive(false);
            tenantRepository.save(tenant);
            return ResponseEntity.ok("Tenant successfully deactivated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> addTags(String id, List<String> tags) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
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
            return ResponseEntity.ok("Tenant successfully added tags");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> removeTags(String id, List<String> tags) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
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
            return ResponseEntity.ok("Tenant successfully removed tags");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> addUsers(String id, List<String> users) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            List<String> oldUsers = tenant.getUsers();
            for (String user : users) {
                if (!oldUsers.contains(user)) {
                    oldUsers.add(user);
                }
            }
            tenant.setUsers(oldUsers);
            tenantRepository.save(tenant);
            return ResponseEntity.ok("Tenant successfully added users");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> removeUsers(String id, List<String> users) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            List<String> oldUsers = tenant.getUsers();
            for (String user : users) {
                if (oldUsers.size() != 1) {
                    oldUsers.remove(user);
                }
            }
            return ResponseEntity.ok("Tenant successfully removed users");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
