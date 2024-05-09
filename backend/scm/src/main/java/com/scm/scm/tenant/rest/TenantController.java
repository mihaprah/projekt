package com.scm.scm.tenant.rest;

import com.scm.scm.tenant.vao.Tenant;
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

    @GetMapping
    public ResponseEntity<List<Tenant>> getTenants() {
        List<Tenant> tenants = tenantServices.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{tenant_id}")
    public ResponseEntity<Tenant> getTenant(@PathVariable("tenant_id") String tenantId) {
        Tenant tenant = tenantServices.getTenantById(tenantId);
        return ResponseEntity.ok(tenant);
    }

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        Tenant createdTenant = tenantServices.addTenant(tenant);
        return ResponseEntity.ok(createdTenant);
    }

    @PutMapping
    public ResponseEntity<Tenant> updateTenant(@RequestBody Tenant tenant) {
        Tenant updatedTenant = tenantServices.updateTenant(tenant);
        return ResponseEntity.ok(updatedTenant);
    }

    @PutMapping("/deactivate/{tenant_id}")
    public ResponseEntity<String> deactivateTenant(@PathVariable("tenant_id") String tenantId) {
        return tenantServices.deactivateTenant(tenantId);
    }

    @PutMapping("/tags/add/{tenant_id}")
    public ResponseEntity<String> addTag(@PathVariable("tenant_id") String tenantId, @RequestBody List<String> tags) {
        return ResponseEntity.ok(tenantServices.addTags(tenantId, tags));
    }

    @PutMapping("/tags/remove/{tenant_id}")
    public ResponseEntity<String> removeTag(@PathVariable("tenant_id") String tenantId, @RequestBody List<String> tags) {
        return ResponseEntity.ok(tenantServices.removeTags(tenantId, tags));
    }

    @PutMapping("/users/add/{tenant_id}")
    public ResponseEntity<String> addUsers(@PathVariable("tenant_id") String tenantId, @RequestBody List<String> users) {
        return ResponseEntity.ok(tenantServices.addUsers(tenantId, users));
    }

    @PutMapping("/users/remove/{tenant_id}")
    public ResponseEntity<String> removeUsers(@PathVariable("tenant_id") String tenantId, @RequestBody List<String> users) {
        return ResponseEntity.ok(tenantServices.removeUsers(tenantId, users));
    }


}
