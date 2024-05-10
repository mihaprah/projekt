package com.scm.scm.contact.services;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import com.scm.scm.tenant.services.TenantServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoTemplateService mongoTemplateService;
    private TenantServices tenantServices;

    public Contact findOneContact(String tenantUniqueName, String contactId) {
        if (contactId.isEmpty() || tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("ContactId or uniqueTenantName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + "_main")) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        Contact contact = mongoTemplate.findById(contactId, Contact.class, tenantUniqueName + "_main");
        if (contact == null) {
            throw new CustomHttpException("Contact not found", 404, ExceptionCause.USER_ERROR);
        }
        return contact;
    }

    public List<Contact> findAllContacts(String tenantUniqueName) {
        if (tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("TenantUniqueName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + "_main")) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        return mongoTemplate.findAll(Contact.class, tenantUniqueName + "_main");
    }

    public String createContact(Contact contact) {
        if (contact.getTenantUniqueName().isEmpty()) {
            throw new CustomHttpException("TenantUniqueName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!contact.getId().isEmpty()) {
            Contact existingContact = mongoTemplate.findById(contact.getId(), Contact.class, contact.getTenantUniqueName() + "_main");
            if (existingContact != null) {
                throw new CustomHttpException("Contact already exists", 400, ExceptionCause.USER_ERROR);
            }
        }
        if (!mongoTemplateService.collectionExists(contact.getTenantUniqueName() + "_main")) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        if (contact.getTitle().isEmpty()) {
            throw new CustomHttpException("Contact title is empty", 400, ExceptionCause.USER_ERROR);
        }
        contact.setId(contact.generateId(contact.getTitle()));
        contact.setAttributesToString(contact.contactAttributesToString());
        mongoTemplate.save(contact, contact.getTenantUniqueName() + "_main");
        tenantServices.addTags(contact.getTenantUniqueName(), contact.getTags());
        return "Contact created successfully to " + contact.getTenantUniqueName() + "_main collection";
    }

    public Contact updateContact(Contact contact) {
        if (contact.getTenantUniqueName().isEmpty()) {
            throw new CustomHttpException("TenantUniqueName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (contact.getId().isEmpty()) {
            throw new CustomHttpException("Contact id is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(contact.getTenantUniqueName() + "_main")) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }

        Contact existingContact = mongoTemplate.findById(contact.getId(), Contact.class, contact.getTenantUniqueName());
        if (existingContact != null) {
            existingContact.setTitle(contact.getTitle());
            existingContact.setComments(contact.getComments());
            existingContact.setTags(contact.getTags());
            existingContact.setProps(contact.getProps());
            existingContact.setAttributesToString(existingContact.contactAttributesToString());

            mongoTemplate.save(existingContact, existingContact.getTenantUniqueName());
            return existingContact;
        } else {
            throw new CustomHttpException("Contact does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public String deleteContact(String tenantUniqueName, String contactId) {
        if (contactId.isEmpty() || tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("ContactId or uniqueTenantName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + "_main") || !mongoTemplateService.collectionExists(tenantUniqueName + "_deleted")) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        Contact contact = mongoTemplate.findById(contactId, Contact.class, tenantUniqueName + "_main");
        if (contact == null) {
            throw new CustomHttpException("Contact not found", 404, ExceptionCause.USER_ERROR);
        }
        mongoTemplate.remove(contact, tenantUniqueName + "_main");
        mongoTemplate.save(contact, tenantUniqueName + "_deleted");
        return "Contact deleted successfully from " + tenantUniqueName + "_main collection";
    }
}
