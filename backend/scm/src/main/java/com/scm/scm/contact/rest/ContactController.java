package com.scm.scm.contact.rest;

import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.contact.vao.Contact;
import com.scm.scm.support.export.ExportContactExcel;
import com.scm.scm.support.export.ExportContactRequest;
import com.scm.scm.support.security.UserAccessService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactServices contactServices;

    @Autowired
    private ExportContactExcel exportContactExcel;

    @Autowired
    private UserAccessService userAccessService;

    @GetMapping("/{contact_id}/{tenant_unique_name}")
    public ResponseEntity<Contact> getContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName) {
        Contact contact = contactServices.findOneContact(tenantUniqueName, id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/{tenant_unique_name}")
    public ResponseEntity<List<Contact>> getContacts(@PathVariable(name = "tenant_unique_name") String tenantUniqueName) {
        List<Contact> contacts = contactServices.findAllContacts(tenantUniqueName);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping
    public ResponseEntity<String> addContact(@RequestBody Contact contact) {
        Contact cleanContact = new Contact();
        cleanContact.setId(StringEscapeUtils.escapeHtml4(contact.getId()));
        cleanContact.setTitle(StringEscapeUtils.escapeHtml4(contact.getTitle()));
        cleanContact.setUser(StringEscapeUtils.escapeHtml4(contact.getUser()));
        cleanContact.setTenantUniqueName(StringEscapeUtils.escapeHtml4(contact.getTenantUniqueName()));
        cleanContact.setComments(StringEscapeUtils.escapeHtml4(contact.getComments()));
        cleanContact.setAttributesToString(StringEscapeUtils.escapeHtml4(contact.getAttributesToString()));

        List<String> sanitizedTags = new ArrayList<>();
        for (String tag : contact.getTags()) {
            sanitizedTags.add(StringEscapeUtils.escapeHtml4(tag));
        }
        cleanContact.setTags(sanitizedTags);

        Map<String, String> sanitizedProps = new HashMap<>();
        for (Map.Entry<String, String> entry : contact.getProps().entrySet()) {
            sanitizedProps.put(StringEscapeUtils.escapeHtml4(entry.getKey()), StringEscapeUtils.escapeHtml4(entry.getValue()));
        }
        cleanContact.setProps(sanitizedProps);

        return ResponseEntity.ok(contactServices.createContact(cleanContact));
    }

    @PutMapping
    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactServices.updateContact(contact));
    }

    @DeleteMapping("/{contact_id}/{tenant_unique_name}")
    public ResponseEntity<String> deleteContact(@PathVariable(name = "contact_id") String id, @PathVariable(name = "tenant_unique_name") String tenantUniqueName) {
        String cleanId = StringEscapeUtils.escapeHtml4(id);
        String cleanTenantUniqueName = StringEscapeUtils.escapeHtml4(tenantUniqueName);

        return ResponseEntity.ok(contactServices.deleteContact(cleanTenantUniqueName, cleanId));
    }

    @PostMapping("/export")
    public ResponseEntity<String> exportContacts(@RequestBody ExportContactRequest request) {
        String user = StringEscapeUtils.escapeHtml4(request.getUser());
        String tenantUniqueName = StringEscapeUtils.escapeHtml4(request.getTenantUniqueName());
        String tenantId = StringEscapeUtils.escapeHtml4(request.getTenantId());
        try {
            if(userAccessService.hasAccessToTenant(user, tenantId)) {
                exportContactExcel.exportContacts(tenantUniqueName);
                return ResponseEntity.ok("Contacts exported successfully for tenant: " + tenantUniqueName);
            } else {
                return ResponseEntity.ok("Contacts NOT exported.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}