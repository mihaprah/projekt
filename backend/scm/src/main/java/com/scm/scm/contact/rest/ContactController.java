package com.scm.scm.contact.rest;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.ImportContactExcel;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactServices contactServices;
    private final ExportContactExcel exportContactExcel;
    private final ImportContactExcel importContactExcel;
    private final UserAccessService userAccessService;
    private final PredefinedSearchServices predefinedSearchServices;
    private final UserVerifyService userVerifyService;

    @Autowired
    public ContactController(ContactServices contactServices, ExportContactExcel exportContactExcel, ImportContactExcel importContactExcel, UserAccessService userAccessService, PredefinedSearchServices predefinedSearchServices, UserVerifyService userVerifyService) {
        this.contactServices = contactServices;
        this.exportContactExcel = exportContactExcel;
        this.importContactExcel = importContactExcel;
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
        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName, false);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping( "/{tenant_unique_name}/deleted")
    public ResponseEntity<List<ContactDTO>> getDeletedContacts(@PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToContact(decodedToken.getEmail(), tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName, true);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addContact(@RequestHeader("userToken") String userToken,@RequestHeader("duplicate") String duplicate, @RequestBody ContactDTO contactDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());
        contactDTO.setUser(sanitizedUserToken);
        Boolean duplicateContact = Boolean.parseBoolean(duplicate);
        if (!userAccessService.hasAccessToContact(sanitizedUserToken, contactDTO.getTenantUniqueName())) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(contactServices.createContact(contactDTO, sanitizedUserToken, duplicateContact));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> updateContact(@RequestHeader("userToken") String userToken, @RequestBody ContactDTO contactDTO) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, contactDTO.getTenantUniqueName())) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(contactServices.updateContact(contactDTO, sanitizedUserToken));
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

        return ResponseEntity.ok(contactServices.deleteContact(cleanTenantUniqueName, cleanId, false, sanitizedUserToken));
    }

    @DeleteMapping("/delete/{contact_id}/{tenant_unique_name}")
    public ResponseEntity<String> deleteContactCompletely(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        String cleanId = StringEscapeUtils.escapeHtml4(id);
        String cleanTenantUniqueName = StringEscapeUtils.escapeHtml4(tenantUniqueName);

        return ResponseEntity.ok(contactServices.deleteContact(cleanTenantUniqueName, cleanId, true, sanitizedUserToken));
    }

    @DeleteMapping("/delete_selected/{tenant_unique_name}")
    public ResponseEntity<String> deleteContacts(@PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestBody List<String> contactIds, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        String cleanTenantUniqueName = StringEscapeUtils.escapeHtml4(tenantUniqueName);
        List<String> sanitizedContactIds = contactIds.stream()
                .map(StringEscapeUtils::escapeHtml4)
                .collect(Collectors.toList());

        return ResponseEntity.ok(contactServices.deleteMultipleContacts(cleanTenantUniqueName, sanitizedContactIds, sanitizedUserToken));
    }

    @PutMapping(value = "/revert/{contact_id}/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> revertContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (!userAccessService.hasAccessToContact(sanitizedUserToken, tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        String cleanId = StringEscapeUtils.escapeHtml4(id);
        String cleanTenantUniqueName = StringEscapeUtils.escapeHtml4(tenantUniqueName);

        return ResponseEntity.ok(contactServices.revertContact(cleanTenantUniqueName, cleanId, sanitizedUserToken));
    }

    @PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportContacts(@RequestBody ExportContactRequest request, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (request == null || sanitizedUserToken == null || request.getTenantUniqueName() == null || request.getTenantId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String tenantUniqueName = StringEscapeUtils.escapeHtml4(request.getTenantUniqueName());
        String tenantId = StringEscapeUtils.escapeHtml4(request.getTenantId());
        List<String> contactIds = request.getContactIds().stream()
                .map(StringEscapeUtils::escapeHtml4)
                .toList();

        if(!userAccessService.hasAccessToTenant(sanitizedUserToken, tenantId) || !userAccessService.hasAccessToContact(sanitizedUserToken, tenantUniqueName)) {
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

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importContacts(@RequestParam("file") MultipartFile file, @RequestParam("tenantUniqueName") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        String sanitizedUserToken = StringEscapeUtils.escapeHtml4(decodedToken.getEmail());

        if (file.isEmpty() || sanitizedUserToken == null) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }

        try {
            importContactExcel.importContactsFromExcel(file, sanitizedUserToken, tenantUniqueName);
            return new ResponseEntity<>("Contacts imported successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.severe("Error occurred during import: " + e.getMessage());
            return new ResponseEntity<>("Failed to import contacts", HttpStatus.INTERNAL_SERVER_ERROR);
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