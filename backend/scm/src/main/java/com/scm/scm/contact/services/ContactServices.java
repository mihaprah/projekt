package com.scm.scm.contact.services;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.vao.Contact;
import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.mongoTemplate.CollectionType;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import com.scm.scm.tenant.services.TenantServices;
import lombok.AllArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoTemplateService mongoTemplateService;
    private TenantServices tenantServices;
    private EventsServices eventsServices;
    private static final Logger log = Logger.getLogger(ContactServices.class.toString());

    private ContactDTO convertToDTO(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .title(contact.getTitle())
                .user(contact.getUser())
                .tenantUniqueName(contact.getTenantUniqueName())
                .comments(contact.getComments())
                .createdAt(contact.getCreatedAt().toString())
                .tags(String.join(",", contact.getTags()))
                .props(contact.getProps().toString())
                .attributesToString(contact.getAttributesToString())
                .build();
    }

    private Contact convertToEntity(ContactDTO contactDTO) {
        return new Contact(
                contactDTO.getId(),
                contactDTO.getTitle(),
                contactDTO.getUser(),
                contactDTO.getTenantUniqueName(),
                contactDTO.getComments(),
                LocalDateTime.parse(contactDTO.getCreatedAt()),
                Arrays.asList(contactDTO.getTags().split(",")),
                new HashMap<>(),
                contactDTO.getAttributesToString()
        );
    }

    public ContactDTO findOneContact(String tenantUniqueName, String contactId) {
        if (contactId.isEmpty() || tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("ContactId or uniqueTenantName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + CollectionType.MAIN.getCollectionType())) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        Contact contact = mongoTemplate.findById(contactId, Contact.class, tenantUniqueName + CollectionType.MAIN.getCollectionType());
        if (contact == null) {
            throw new CustomHttpException("Contact not found", 404, ExceptionCause.USER_ERROR);
        }
        log.info("Contact found with id: " + contactId);
        return convertToDTO(contact);
    }

    public List<ContactDTO> findAllContacts(String tenantUniqueName) {
        if (tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("TenantUniqueName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + CollectionType.MAIN.getCollectionType())) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        log.info("All contacts found for tenant: " + tenantUniqueName);
        List<Contact> contacts = mongoTemplate.findAll(Contact.class, tenantUniqueName + CollectionType.MAIN.getCollectionType());
        return contacts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public String createContact(ContactDTO contactDTO) {
        ContactDTO sanitizedContactDTO = new ContactDTO();
        sanitizedContactDTO.setId(StringEscapeUtils.escapeHtml4(contactDTO.getId()));
        sanitizedContactDTO.setTitle(StringEscapeUtils.escapeHtml4(contactDTO.getTitle()));
        sanitizedContactDTO.setUser(StringEscapeUtils.escapeHtml4(contactDTO.getUser()));
        sanitizedContactDTO.setTenantUniqueName(StringEscapeUtils.escapeHtml4(contactDTO.getTenantUniqueName()));
        sanitizedContactDTO.setComments(StringEscapeUtils.escapeHtml4(contactDTO.getComments()));
        sanitizedContactDTO.setCreatedAt(StringEscapeUtils.escapeHtml4(contactDTO.getCreatedAt()));
        sanitizedContactDTO.setTags(StringEscapeUtils.escapeHtml4(contactDTO.getTags()));
        sanitizedContactDTO.setProps(StringEscapeUtils.escapeHtml4(contactDTO.getProps()));
        sanitizedContactDTO.setAttributesToString(StringEscapeUtils.escapeHtml4(contactDTO.getAttributesToString()));

        Contact contact = convertToEntity(sanitizedContactDTO);
        if (contact.getTenantUniqueName().isEmpty()) {
            throw new CustomHttpException("TenantUniqueName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!contact.getId().isEmpty()) {
            Contact existingContact = mongoTemplate.findById(contact.getId(), Contact.class, contact.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
            if (existingContact != null) {
                throw new CustomHttpException("Contact already exists", 400, ExceptionCause.USER_ERROR);
            }
        }
        if (!mongoTemplateService.collectionExists(contact.getTenantUniqueName() + CollectionType.MAIN.getCollectionType())) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        if (contact.getTitle().isEmpty()) {
            throw new CustomHttpException("Contact title is empty", 400, ExceptionCause.USER_ERROR);
        }
        contact.setId(contact.generateId(contact.getTitle()));
        contact.setAttributesToString(contact.contactAttributesToString());
        mongoTemplate.save(contact, contact.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
        tenantServices.addTags(contact.getTenantUniqueName(), contact.getTags());

        Event event = new Event(contact.getUser(), contact.getId(), EventState.CREATED);
        eventsServices.addEvent(event, contact.getTenantUniqueName());

        log.info("Contact created with id: " + contact.getId() + " for tenant: " + contact.getTenantUniqueName());
        return "Contact created successfully to " + contact.getTenantUniqueName() + "_main collection";
    }

    public ContactDTO updateContact(ContactDTO contactDTO) {
        Contact contact = convertToEntity(contactDTO);
        if (contact.getTenantUniqueName().isEmpty()) {
            throw new CustomHttpException("TenantUniqueName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (contact.getId().isEmpty()) {
            throw new CustomHttpException("Contact id is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(contact.getTenantUniqueName() + CollectionType.MAIN.getCollectionType())) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }

        Contact existingContact = mongoTemplate.findById(contact.getId(), Contact.class, contact.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
        if (existingContact != null) {
            if(!existingContact.getTitle().equals(contact.getTitle())){
                Event event = new Event();
                event.setUser(contact.getUser());
                event.setContact(existingContact.getId());
                event.setEventState(EventState.UPDATED);
                event.setPropKey("Title");
                event.setPrevState(existingContact.getTitle());
                event.setCurrentState(contact.getTitle());
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());
            }
            existingContact.setTitle(contact.getTitle());
            existingContact.setComments(contact.getComments());

            checkTags(existingContact, contact);
            existingContact.setTags(contact.getTags());

            checkProps(existingContact, contact);
            existingContact.setProps(contact.getProps());
            existingContact.setAttributesToString(existingContact.contactAttributesToString());

            mongoTemplate.save(existingContact, existingContact.getTenantUniqueName());
            log.info("Contact updated with id: " + contact.getId() + " for tenant: " + contact.getTenantUniqueName());
            return convertToDTO(existingContact);
        } else {
            throw new CustomHttpException("Contact does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
    }

    public String deleteContact(String tenantUniqueName, String contactId) {
        if (contactId.isEmpty() || tenantUniqueName.isEmpty()) {
            throw new CustomHttpException("ContactId or uniqueTenantName is empty", 400, ExceptionCause.USER_ERROR);
        }
        if (!mongoTemplateService.collectionExists(tenantUniqueName + CollectionType.MAIN.getCollectionType()) || !mongoTemplateService.collectionExists(tenantUniqueName + "_deleted")) {
            throw new CustomHttpException("Collection does not exist", 500, ExceptionCause.SERVER_ERROR);
        }
        Contact contact = mongoTemplate.findById(contactId, Contact.class, tenantUniqueName + CollectionType.MAIN.getCollectionType());
        if (contact == null) {
            throw new CustomHttpException("Contact not found", 404, ExceptionCause.USER_ERROR);
        }
        mongoTemplate.remove(contact, tenantUniqueName + CollectionType.MAIN.getCollectionType());
        log.info("Contact deleted with id: " + contactId + " for tenant: " + tenantUniqueName);
        mongoTemplate.save(contact, tenantUniqueName + CollectionType.DELETED.getCollectionType());
        log.info("Contact saved to " + tenantUniqueName + "_deleted collection");

        Event event = new Event(contact.getUser(), contact.getId(), EventState.DELETED);
        eventsServices.addEvent(event, contact.getTenantUniqueName());

        tenantServices.removeTags(tenantUniqueName, contact.getTags());

        return "Contact deleted successfully from " + tenantUniqueName + "_main collection";
    }

    private void checkProps (Contact existingContact, Contact contact){
        Map<String, String> existingProps = existingContact.getProps();
        Map<String, String> props = contact.getProps();

        Event event = new Event();
        event.setUser(contact.getUser());
        event.setContact(existingContact.getId());

        for (String key : props.keySet()){
            if(existingProps.containsKey(key)){
                String existingValue = existingProps.get(key);
                String value = props.get(key);
                if(!existingValue.equals(value)){
                    event.setEventState(EventState.UPDATED);
                    event.setPropKey(key);
                    event.setPrevState(existingValue);
                    event.setCurrentState(value);
                    eventsServices.addEvent(event, existingContact.getTenantUniqueName());
                }
            } else {
                event.setEventState(EventState.PROP_ADD);
                event.setPropKey(key);
                event.setPrevState("");
                event.setCurrentState(props.get(key));
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());
            }
        }
        for (String key : existingProps.keySet()){
            if (!props.containsKey(key)){
                event.setEventState(EventState.PROP_REMOVED);
                event.setPropKey(key);
                event.setPrevState(existingProps.get(key));
                event.setCurrentState("");
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());
            }
        }
    }
    private void checkTags (Contact existingContact, Contact contact) {
        List<String> existingTags = existingContact.getTags();
        List<String> tags = contact.getTags();

        Event event = new Event();
        event.setUser(contact.getUser());
        event.setContact(existingContact.getId());

        for (String tag : tags){
            if(!existingTags.contains(tag)){
                event.setEventState(EventState.TAG_ADD);
                event.setPropKey("TAG");
                event.setPrevState("");
                event.setCurrentState(tag);
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());

                tenantServices.addTags(contact.getTenantUniqueName(), contact.getTags());
            }
        }
        for ( String existingTag : existingTags){
            if(!tags.contains(existingTag)){
                event.setEventState(EventState.TAG_REMOVED);
                event.setPropKey("TAG");
                event.setPrevState(existingTag);
                event.setCurrentState("");
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());

                tenantServices.removeTags(contact.getTenantUniqueName(), contact.getTags());
            }
        }
    }
}