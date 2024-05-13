package com.scm.scm.tenant.rest;

import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.services.TenantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantServices tenantServices;
    @Autowired
    private UserAccessService userAccessService;

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getTenants() {
        List<TenantDTO> tenants = tenantServices.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{tenant_id}/{user_token}")
    public ResponseEntity<TenantDTO> getTenant(@PathVariable("tenant_id") String tenantId, @PathVariable("user_token") String user_token) {
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantId);
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        TenantDTO tenant = tenantServices.getTenantById(tenantId);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("/user/{user_token}")
    public ResponseEntity<List<TenantDTO>> getTenantsByUser(@PathVariable("user_token") String user_token) {
        List<TenantDTO> tenants = tenantServices.getTenantsByUser(user_token);
        return ResponseEntity.ok(tenants);
    }

    @PostMapping
    public ResponseEntity<TenantDTO> createTenant(@RequestBody TenantDTO tenantDTO) {
        TenantDTO createdTenant = tenantServices.addTenant(tenantDTO);
        return ResponseEntity.ok(createdTenant);
    }

    @PutMapping("/{user_token}")
    public ResponseEntity<TenantDTO> updateTenant(@PathVariable("user_token") String user_token, @RequestBody TenantDTO tenantDTO) {
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantDTO.getId());
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        TenantDTO updatedTenant = tenantServices.updateTenant(tenantDTO);
        return ResponseEntity.ok(updatedTenant);
    }

    @PutMapping("/deactivate/{tenant_id}/{user_token}")
    public ResponseEntity<String> deactivateTenant(@PathVariable("tenant_id") String tenantId, @PathVariable("user_token") String user_token){
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantId);
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.deactivateTenant(tenantId));
    }

    @PutMapping("/tags/add/{tenant_id}/{user_token}")
    public ResponseEntity<String> addTag(@PathVariable("tenant_id") String tenantId, @PathVariable("user_token") String user_token, @RequestBody List<String> tags) {
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantId);
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.addTags(tenantId, tags));
    }

    @PutMapping("/tags/remove/{tenant_id}/{user_token}")
    public ResponseEntity<String> removeTag(@PathVariable("tenant_id") String tenantId, @PathVariable("user_token") String user_token, @RequestBody List<String> tags) {
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantId);
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.removeTags(tenantId, tags));
    }

    @PutMapping("/users/add/{tenant_id}/{user_token}")
    public ResponseEntity<String> addUsers(@PathVariable("tenant_id") String tenantId, @PathVariable("user_token") String user_token, @RequestBody List<String> users) {
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantId);
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.addUsers(tenantId, users));
    }

    @PutMapping("/users/remove/{tenant_id}/{user_token}")
    public ResponseEntity<String> removeUsers(@PathVariable("tenant_id") String tenantId, @PathVariable("user_token") String user_token, @RequestBody List<String> users) {
        boolean check = userAccessService.hasAccessToTenant(user_token, tenantId);
        if (!check) {
            throw new CustomHttpException("User does not have access to this tenant", 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.removeUsers(tenantId, users));
    }
}