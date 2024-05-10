package com.scm.scm.contact.rest;

import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.contact.vao.Contact;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        contact.setId(StringEscapeUtils.escapeHtml4(contact.getId()));
        contact.setTitle(StringEscapeUtils.escapeHtml4(contact.getTitle()));
        contact.setUser(StringEscapeUtils.escapeHtml4(contact.getUser()));
        contact.setTenantUniqueName(StringEscapeUtils.escapeHtml4(contact.getTenantUniqueName()));
        contact.setComments(StringEscapeUtils.escapeHtml4(contact.getComments()));
        contact.setAttributesToString(StringEscapeUtils.escapeHtml4(contact.getAttributesToString()));

        List<String> sanitizedTags = new ArrayList<>();
        for (String tag : contact.getTags()) {
            sanitizedTags.add(StringEscapeUtils.escapeHtml4(tag));
        }
        contact.setTags(sanitizedTags);

        Map<String, String> sanitizedProps = new HashMap<>();
        for (Map.Entry<String, String> entry : contact.getProps().entrySet()) {
            sanitizedProps.put(StringEscapeUtils.escapeHtml4(entry.getKey()), StringEscapeUtils.escapeHtml4(entry.getValue()));
        }
        contact.setProps(sanitizedProps);

        return ResponseEntity.ok(contactServices.createContact(contact));
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
}