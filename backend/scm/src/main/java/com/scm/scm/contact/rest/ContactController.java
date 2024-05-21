package com.scm.scm.contact.rest;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.support.security.UserVerifyService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactServices contactServices;
    private final ExportContactExcel exportContactExcel;
    private final UserAccessService userAccessService;
    private final PredefinedSearchServices predefinedSearchServices;
    private final UserVerifyService userVerifyService;

    @Autowired
    public ContactController(ContactServices contactServices, ExportContactExcel exportContactExcel, UserAccessService userAccessService, PredefinedSearchServices predefinedSearchServices, UserVerifyService userVerifyService) {
        this.contactServices = contactServices;
        this.exportContactExcel = exportContactExcel;
        this.userAccessService = userAccessService;
        this.predefinedSearchServices = predefinedSearchServices;
        this.userVerifyService = userVerifyService;
    }

    private static final Logger log = Logger.getLogger(ContactServices.class.toString());

    @GetMapping(value = "/{contact_id}/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> getContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToContact(decodedToken.getEmail(), tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        ContactDTO contactDTO = contactServices.findOneContact(tenantUniqueName, id);
        return ResponseEntity.ok(contactDTO);
    }

    @GetMapping( "/{tenant_unique_name}")
    public ResponseEntity<List<ContactDTO>> getContacts(@PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToContact(decodedToken.getEmail(), tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addContact(@RequestHeader("userToken") String userToken, @RequestBody ContactDTO contactDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());
        contactDTO.setUser(sanitizedUserToken);

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, contactDTO.getTenantUniqueName())) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(contactServices.createContact(contactDTO));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> updateContact(@RequestHeader("userToken") String userToken, @RequestBody ContactDTO contactDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, contactDTO.getTenantUniqueName())) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(contactServices.updateContact(contactDTO));
    }

    @DeleteMapping("/{contact_id}/{tenant_unique_name}")
    public ResponseEntity<String> deleteContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        String cleanId = StringEscapeUtils.escapeHtml4(id);
        String cleanTenantUniqueName = StringEscapeUtils.escapeHtml4(tenantUniqueName);

        return ResponseEntity.ok(contactServices.deleteContact(cleanTenantUniqueName, cleanId));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportContacts(@RequestBody ExportContactRequest request) {
        if (request == null || request.getUserToken() == null || request.getTenantUniqueName() == null || request.getTenantId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String user = StringEscapeUtils.escapeHtml4(request.getUserToken());
        String tenantUniqueName = StringEscapeUtils.escapeHtml4(request.getTenantUniqueName());
        String tenantId = StringEscapeUtils.escapeHtml4(request.getTenantId());
        List<String> contactIds = request.getContactIds().stream()
                .map(StringEscapeUtils::escapeHtml4)
                .toList();

        if(!userAccessService.hasAccessToTenant(user, tenantId) || !userAccessService.hasAccessToContact(user, tenantUniqueName)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        try {
            log.log(Level.INFO, "Contacts exported successfully for tenant: {0}", tenantUniqueName);
            return exportContactExcel.exportContacts(tenantUniqueName, contactIds);
        } catch (IllegalArgumentException e) {
            log.severe("Error occurred during export: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/search/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContactDTO>> searchContacts(@PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken, @RequestBody PredefinedSearchDTO searchDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        PredefinedSearch search = predefinedSearchServices.convertToEntity(searchDTO);
        return ResponseEntity.ok(contactServices.getContactsBySearch(search));
    }
}