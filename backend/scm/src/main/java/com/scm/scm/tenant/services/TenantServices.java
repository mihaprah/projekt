package com.scm.scm.tenant.services;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import com.scm.scm.support.mongoTemplate.CollectionType;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import com.scm.scm.tenant.dao.TenantRepository;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.vao.Tenant;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class TenantServices {

    private TenantRepository tenantRepository;
    private MongoTemplateService mongoTemplateService;
    private MongoTemplate mongoTemplate;
    private static final Logger log = Logger.getLogger(TenantServices.class.toString());

    private TenantDTO convertToDTO(Tenant tenant) {
        return TenantDTO.builder()
                .id(tenant.getId())
                .title(tenant.getTitle())
                .tenantUniqueName(tenant.getTenantUniqueName())
                .description(tenant.getDescription())
                .colorCode(tenant.getColorCode())
                .active(tenant.isActive())
                .users(tenant.getUsers())
                .contactTags(tenant.getContactTags())
                .build();
    }

    private Tenant convertToEntity(TenantDTO tenantDTO) {
        return Tenant.builder()
                .id(tenantDTO.getId())
                .title(tenantDTO.getTitle())
                .tenantUniqueName(tenantDTO.getTenantUniqueName())
                .description(tenantDTO.getDescription())
                .colorCode(tenantDTO.getColorCode())
                .active(tenantDTO.isActive())
                .users(tenantDTO.getUsers())
                .contactTags(tenantDTO.getContactTags())
                .build();
    }

    public TenantDTO addTenant(TenantDTO tenantDTO) {
        Tenant tenant = convertToEntity(tenantDTO);
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
                tenant.setLabels(setPredefinedLabels());
                tenantRepository.save(tenant);
                log.log(Level.INFO, "Tenant created with id: {0}", tenant.getId());
                return convertToDTO(tenant);
            }
        }
    }

    public List<TenantDTO> getAllTenants() {
        List<Tenant> tenants = tenantRepository.findAll();
        log.log(Level.INFO, "All tenants found");
        return tenants.stream().map(this::convertToDTO).toList();
    }

    public TenantDTO getTenantById(String id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        log.log(Level.INFO, "Tenant found with id: {0}", id);
        return convertToDTO(tenant);
    }

    public TenantDTO updateTenant(TenantDTO tenantDTO) {
        Tenant tenant = convertToEntity(tenantDTO);
        Tenant oldTenant = tenantRepository.findById(tenant.getId()).orElse(null);
        if (oldTenant != null) {
            oldTenant.setDescription(tenant.getDescription());
            oldTenant.setColorCode(tenant.getColorCode());
            oldTenant.setTitle(tenant.getTitle());
            tenantRepository.save(oldTenant);
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR);
        }
        return convertToDTO(oldTenant);
    }

    public String deactivateTenant(String id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            tenant.setActive(false);
            tenantRepository.save(tenant);
            return "Tenant successfully deactivated";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NULL.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
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
            return "Tenant successfully added tags";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR);
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
            return "Tenant successfully removed tags";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR);
        }
    }

    public String addUsers(String id, List<String> users) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.SERVER_ERROR));
        int userCount = 0;
        if (tenant != null) {
            List<String> oldUsers = tenant.getUsers();
            for (String user : users) {
                if (!oldUsers.contains(user)) {
                    oldUsers.add(user);
                } else {
                    userCount++;
                }
            }
            tenant.setUsers(oldUsers);
            tenantRepository.save(tenant);
            if (userCount == users.size()) {
                return "No new users added";
            } else if (userCount > 0) {
                return "New users added, but some users already exist in the tenant";
            }
            return "Users added to Tenant successfully";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NULL.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public String removeUsers(String id, List<String> users) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        int usersNotRemovedCount = 0;
        if (tenant != null) {
            List<String> oldUsers = tenant.getUsers();
            for (String user : users) {
                if (oldUsers.contains(user)) {
                    if (oldUsers.size() == 1) {
                        throw new CustomHttpException("At least one user must remain in the tenant", 400, ExceptionCause.USER_ERROR);
                    }
                    oldUsers.remove(user);
                } else {
                    usersNotRemovedCount++;
                }
            }
            tenant.setUsers(oldUsers);
            tenantRepository.save(tenant);
            if (usersNotRemovedCount > 0) {
                return "Some users not removed as they do not exist in the tenant";
            }
            return "Users removed from Tenant successfully";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NULL.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public List<TenantDTO> getTenantsByUser(String username) {
        List<Tenant> tenants = tenantRepository.findByUsersContaining(username);
        if (tenants.isEmpty()) {
            throw new CustomHttpException("No tenants found for username " + username, 404, ExceptionCause.USER_ERROR);
        }
        log.log(Level.INFO, "All tenants for username {0} found", username);
        return tenants.stream().map(this::convertToDTO).toList();
    }

    public String addTagsToMultipleContacts(String tenantUniqueName, List<String> contactIds, String tag) {
        if (contactIds.isEmpty()) {
            throw new CustomHttpException("Contact ids cannot be empty", 400, ExceptionCause.USER_ERROR);
        }
        if (tag.isEmpty()) {
            throw new CustomHttpException("Tag cannot be empty", 400, ExceptionCause.USER_ERROR);
        }
        if (tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("Tenant unique name cannot be empty", 400, ExceptionCause.USER_ERROR);
        }
        List<Contact> contacts = mongoTemplate.findAll(Contact.class, tenantUniqueName + CollectionType.MAIN.getCollectionType());
        for (Contact c : contacts) {
            if (contactIds.contains(c.getId())) {
                List<String> oldTags = c.getTags();
                if (!oldTags.contains(tag)) {
                    oldTags.add(tag);
                }
                c.setTags(oldTags);
                mongoTemplate.save(c, tenantUniqueName + CollectionType.MAIN.getCollectionType());
                addTags(tenantUniqueName, List.of(tag));
            }
        }
        return "Tags added to contacts successfully";
    }

    private Map<String, String> setPredefinedLabels() {
        String FILE_PATH = "./src/main/resources/PredefinedLabels.txt";
        List<String> keys = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                keys.add(line);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error reading file: {0}", e.getMessage());
        }
        Map<String, String> labels = new java.util.HashMap<>();
        for (String key : keys) {
            labels.put(key, "");
        }
        log.info("Predefined labels set");
        return labels;
    }

    public String updateLabels(String tenantId, Map<String, String> newLabels) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            Map<String, String> currentLabels = tenant.getLabels();
            currentLabels.putAll(newLabels);
            tenant.setLabels(currentLabels);
            tenantRepository.save(tenant);
            log.log(Level.INFO, "Labels updated for tenant with id: {0}", tenantId);
            return "Labels updated successfully";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NULL.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public void addLabels(String tenantUniqueName, Set<String> newKeys) {
        Tenant tenant = tenantRepository.findByTenantUniqueName(tenantUniqueName);
        if (tenant != null) {
            if (tenant.getLabels() != null) {
                Map<String, String> currentLabels = tenant.getLabels();
                for (String key : newKeys) {
                    if (!currentLabels.containsKey(key)) continue;
                    currentLabels.put(key, "");
                }
                tenant.setLabels(currentLabels);
                tenantRepository.save(tenant);
                log.log(Level.INFO, "Labels added for tenant with tenantUniqueName: {0}", tenantUniqueName);
            }
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NULL.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public String updateDisplayProps(String tenantId, List<String> newProps) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new CustomHttpException(ExceptionMessage.TENANT_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        if (tenant != null) {
            tenant.setDisplayProps(newProps);
            tenantRepository.save(tenant);
            log.log(Level.INFO, "Display properties updated for tenant with id: {0}", tenantId);
            return "Display properties updated successfully";
        } else {
            throw new CustomHttpException(ExceptionMessage.TENANT_NULL.getExceptionMessage(), 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public TenantDTO getTenantByUniqueName(String tenantUniqueName) {
        Tenant tenant = tenantRepository.findByTenantUniqueName(tenantUniqueName);
        if (tenant == null) {
            throw new CustomHttpException("Tenant not found", 404, ExceptionCause.USER_ERROR);
        }
        return convertToDTO(tenant);
    }
}
