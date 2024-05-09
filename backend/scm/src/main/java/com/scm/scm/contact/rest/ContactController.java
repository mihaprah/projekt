package com.scm.scm.contact.rest;

import com.scm.scm.contact.services.ContactServices;
import com.scm.scm.contact.vao.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> addContact(@RequestBody Contact contact){
        return ResponseEntity.ok(contactServices.createContact(contact));
    }

}