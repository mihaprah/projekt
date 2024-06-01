package com.scm.scm.tenant.rest;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.support.security.UserVerifyService;
import com.scm.scm.tenant.dto.TenantDTO;
import com.scm.scm.tenant.services.TenantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    private final TenantServices tenantServices;
    private final UserAccessService userAccessService;
    private final UserVerifyService userVerifyService;


    @Autowired
    public TenantController(TenantServices tenantServices, UserAccessService userAccessService, UserVerifyService userVerifyService) {
        this.tenantServices = tenantServices;
        this.userAccessService = userAccessService;
        this.userVerifyService = userVerifyService;

    }

    @GetMapping(value = "/{tenant_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TenantDTO> getTenant(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        TenantDTO tenant = tenantServices.getTenantById(tenantId);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping(value = "/unique/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TenantDTO> getTenantByUniqueName(@PathVariable("tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToContact(decodedToken.getEmail(), tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        TenantDTO tenant = tenantServices.getTenantByUniqueName(tenantUniqueName);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TenantDTO>> getTenantsByUser(@RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        List<TenantDTO> tenants = tenantServices.getTenantsByUser(decodedToken.getEmail());

        List<TenantDTO> activeTenants = tenants.stream()
                .filter(TenantDTO::isActive)
                .collect(Collectors.toList());

        return ResponseEntity.ok(activeTenants);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TenantDTO> createTenant(@RequestHeader("userToken") String userToken, @RequestBody TenantDTO tenantDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        tenantDTO.setActive(true);
        TenantDTO createdTenant = tenantServices.addTenant(tenantDTO);
        return ResponseEntity.ok(createdTenant);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TenantDTO> updateTenant(@RequestHeader("userToken") String userToken, @RequestBody TenantDTO tenantDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantDTO.getId())) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        TenantDTO updatedTenant = tenantServices.updateTenant(tenantDTO);
        return ResponseEntity.ok(updatedTenant);
    }

    @PutMapping(value = "/deactivate/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deactivateTenant(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.deactivateTenant(tenantId));
    }

    @PutMapping(value = "/tags/add/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTag(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken, @RequestBody List<String> tags) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.addTags(tenantId, tags));
    }

    @PutMapping(value = "/tags/remove/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeTag(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken, @RequestBody List<String> tags) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.removeTags(tenantId, tags));
    }

    @PutMapping(value = "/users/add/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUsers(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken, @RequestBody List<String> users) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.addUsers(tenantId, users));
    }

    @PutMapping(value = "/users/remove/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeUsers(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken, @RequestBody List<String> users) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.removeUsers(tenantId, users));
    }

    @PutMapping(value = "/tags/multiple/add/{tenant_unique_name}/{tag}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addMultipleTags(@PathVariable("tenant_unique_name") String tenantUniqueName, @PathVariable("tag") String[] tags, @RequestHeader("userToken") String userToken, @RequestHeader("tenantId") String tenantId, @RequestBody List<String> contactIds) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.addTagsToMultipleContacts(tenantUniqueName, contactIds, tags, decodedToken.getEmail()));
    }

    @PutMapping(value = "/tags/multiple/remove/{tenant_unique_name}/{tag}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeMultipleTags(
            @PathVariable("tenant_unique_name") String tenantUniqueName,
            @PathVariable("tag") String[] tags,
            @RequestHeader("userToken") String userToken,
            @RequestHeader("tenantId") String tenantId,
            @RequestBody List<String> contactIds) {

        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }

        return ResponseEntity.ok(tenantServices.removeTagsFromMultipleContacts(tenantUniqueName, contactIds, tags, decodedToken.getEmail()));
    }


    @PutMapping(value = "/props/multiple/add/{tenant_unique_name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addMultipleProps(
            @PathVariable("tenant_unique_name") String tenantUniqueName,
            @RequestHeader("userToken") String userToken,
            @RequestHeader("tenantId") String tenantId,
            @RequestBody Map<String, Object> requestBody) {

        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }

        List<String> contactIds = (List<String>) requestBody.get("contactIds");
        Map<String, String> propData = (Map<String, String>) requestBody.get("propData");

        return ResponseEntity.ok(tenantServices.addPropsToMultipleContacts(tenantUniqueName, contactIds, propData, decodedToken.getEmail()));
    }

    @PutMapping(value = "/props/multiple/remove/{tenant_unique_name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeMultipleProps(
            @PathVariable("tenant_unique_name") String tenantUniqueName,
            @RequestHeader("userToken") String userToken,
            @RequestHeader("tenantId") String tenantId,
            @RequestBody Map<String, Object> requestBody) {

        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }

        List<String> contactIds = (List<String>) requestBody.get("contactIds");
        List<String> propsToRemove = (List<String>) requestBody.get("propsToRemove");

        return ResponseEntity.ok(tenantServices.removePropsFromMultipleContacts(tenantUniqueName, contactIds, propsToRemove, decodedToken.getEmail()));
    }


    @PutMapping(value = "/labels/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateLabels(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken, @RequestBody Map<String, String> labels) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.updateLabels(tenantId, labels));
    }

    @PutMapping(value = "/displayProps/{tenant_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDisplayProps(@PathVariable("tenant_id") String tenantId, @RequestHeader("userToken") String userToken, @RequestBody List<String> displayProps) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(tenantServices.updateDisplayProps(tenantId, displayProps));
    }

    @GetMapping(value = "/size/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getTenantSize(@PathVariable("tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToContact(decodedToken.getEmail(), tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        int size = tenantServices.getContactAmount(tenantUniqueName);
        return ResponseEntity.ok(size);
    }
}