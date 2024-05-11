package com.scm.scm.contact.rest;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactServices contactServices;

    @Autowired
    private ExportContactExcel exportContactExcel;

    @Autowired
    private UserAccessService userAccessService;

    private static final Logger log = Logger.getLogger(ContactServices.class.toString());

    @GetMapping("/{contact_id}/{tenant_unique_name}/{user_token}")
    public ResponseEntity<ContactDTO> getContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName, @PathVariable(name = "user_token") String userToken) {
        boolean check = userAccessService.hasAccessToContact(userToken, tenantUniqueName);
        if (!check) {
            return ResponseEntity.status(403).build();
        }
        ContactDTO contactDTO = contactServices.findOneContact(tenantUniqueName, id);
        return ResponseEntity.ok(contactDTO);
    }

    @GetMapping("/{tenant_unique_name}/{user_token}")
    public ResponseEntity<List<ContactDTO>> getContacts(@PathVariable(name = "tenant_unique_name") String tenantUniqueName, @PathVariable(name = "user_token") String userToken) {
        boolean check = userAccessService.hasAccessToContact(userToken, tenantUniqueName);
        if (!check) {
            return ResponseEntity.status(403).build();
        }
        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping("/{user_token}")
    public ResponseEntity<String> addContact(@PathVariable("user_token") String userToken, @RequestBody ContactDTO contactDTO) {
        boolean check = userAccessService.hasAccessToContact(userToken, contactDTO.getTenantUniqueName());
        if (!check) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(contactServices.createContact(contactDTO));
    }

    @PutMapping("/{user_token}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable("user_token") String userToken, @RequestBody ContactDTO contactDTO) {
        boolean check = userAccessService.hasAccessToContact(userToken, contactDTO.getTenantUniqueName());
        if (!check) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(contactServices.updateContact(contactDTO));
    }

    @DeleteMapping("/{contact_id}/{tenant_unique_name}/{user_token}")
    public ResponseEntity<String> deleteContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName, @PathVariable(name = "user_token") String userToken) {
        boolean check = userAccessService.hasAccessToContact(userToken, tenantUniqueName);
        if (!check) {
            return ResponseEntity.status(403).build();
        }
        String cleanId = StringEscapeUtils.escapeHtml4(id);
        String cleanTenantUniqueName = StringEscapeUtils.escapeHtml4(tenantUniqueName);

        return ResponseEntity.ok(contactServices.deleteContact(cleanTenantUniqueName, cleanId));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportContacts(@RequestBody ExportContactRequest request) {
        String user = StringEscapeUtils.escapeHtml4(request.getUser());
        String tenantUniqueName = StringEscapeUtils.escapeHtml4(request.getTenantUniqueName());
        String tenantId = StringEscapeUtils.escapeHtml4(request.getTenantId());
        try {
            if(userAccessService.hasAccessToTenant(user, tenantId)) {
                exportContactExcel.exportContacts(tenantUniqueName);
                log.info("Contacts exported successfully for tenant: " + tenantUniqueName);
                return exportContactExcel.exportContacts(tenantUniqueName);
            } else {
                return ResponseEntity.status(403).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}