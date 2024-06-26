package com.scm.scm.loadDatabase;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.support.mongoTemplate.CollectionType;
import com.scm.scm.tenant.services.TenantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class LoadContacts {

    private static final Logger log = Logger.getLogger(LoadContacts.class.toString());
    private static final String HOBBY = "Hobby";
    private static final String USER3 = "user3@example.com";

    private final MongoTemplate mongoTemplate;

    private final TenantServices tenantServices;

    private final LoadEvents loadEvents;

    private static final SecureRandom random = new SecureRandom();

    private boolean load10k = false;

    @Autowired
    public LoadContacts(MongoTemplate mongoTemplate, TenantServices tenantServices, LoadEvents loadEvents) {
        this.mongoTemplate = mongoTemplate;
        this.tenantServices = tenantServices;
        this.loadEvents = loadEvents;
    }

    public void createContacts(String[] tenantUniqueNames) {

        List<String> allTags = new ArrayList<>();
        allTags.add("New");
        allTags.add("Important");
        allTags.add("Old");
        allTags.add("CEO");
        allTags.add("CFO");
        allTags.add("Regular");
        allTags.add("VIP");
        allTags.add("Assistant");

        Map<String, String> props = new HashMap<>();
        props.put("Name", "John");
        props.put("Email", "john@gmail.com");
        props.put("Phone", "+7(90)988888");
        props.put("Address", "123 Main St");
        props.put("Company", "XYZ Corp");
        props.put("Position", "Senior Marketing Manager");
        props.put("OfficeLocation", "Downtown");
        props.put(HOBBY, "Photography");
        props.put("SocialMedia", "@john_photographer");
        props.put("Website", "www.johnphotography.com");
        props.put("EmergencyContact", "Jane Doe");
        props.put("Relationship", "Spouse");
        props.put("EmergencyPhone", "+7(90)987654");

        if (load10k) {
            for (int i = 0; i < 10000; i++) {
                List<String> tags = selectRandomTags(new ArrayList<>(allTags), 4);
                Contact contact = new Contact("", "Contact " + (i + 1), "user@example.com", tenantUniqueNames[0], "", LocalDateTime.now(), tags, selectRandomProps(props, 4), "");
                contact.setId(contact.generateId(contact.getTitle()));
                contact.setAttributesToString(contact.contactAttributesToString());
                mongoTemplate.save(contact, contact.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
                Event event = new Event(contact.getUser(), contact.getId(), EventState.CREATED);
                loadEvents.createEvent(event, contact.getTenantUniqueName());
                tenantServices.addTags(contact.getTenantUniqueName(), contact.getTags());
            }
            log.info("Loaded 10,000 test Contacts into the database.");
        } else {
            List<String> tags1 = selectRandomTags(allTags, 4);
            List<String> tags2 = selectRandomTags(allTags, 2);
            List<String> tags3 = selectRandomTags(allTags, 5);
            List<String> tags4 = selectRandomTags(allTags, 3);

            Contact contact1 = new Contact("", "Contact 1", "user1@example.com", tenantUniqueNames[0], "", LocalDateTime.now(), tags1, selectRandomProps(props, 4), "");
            contact1.setId(contact1.generateId(contact1.getTitle()));
            contact1.setAttributesToString(contact1.contactAttributesToString());

            Contact contact2 = new Contact("", "Contact 2", "user1@example.com", tenantUniqueNames[0], "", LocalDateTime.now(), tags2, selectRandomProps(props, 4), "");
            contact2.setId(contact2.generateId(contact2.getTitle()));
            contact2.setAttributesToString(contact2.contactAttributesToString());

            Contact contact3 = new Contact("", "Contact 3", USER3, tenantUniqueNames[1], "", LocalDateTime.now(), tags3, selectRandomProps(props, 4), "");
            contact3.setId(contact3.generateId(contact3.getTitle()));
            contact3.setAttributesToString(contact3.contactAttributesToString());

            Contact contact4 = new Contact("", "Contact 4", USER3, tenantUniqueNames[1], "", LocalDateTime.now(), tags4, selectRandomProps(props, 4), "");
            contact4.setId(contact4.generateId(contact4.getTitle()));
            contact4.setAttributesToString(contact4.contactAttributesToString());

            mongoTemplate.save(contact1, contact1.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
            Event event1 = new Event(contact1.getUser(), contact1.getId(), EventState.CREATED);
            loadEvents.createEvent(event1, contact1.getTenantUniqueName());

            String exTitle = contact1.getTitle();
            contact1.setTitle("Contact 1.1");
            Event event11 = new Event(contact1.getUser(), contact1.getId(), EventState.UPDATED);
            event11.setPropKey("Title");
            event11.setPrevState(exTitle);
            event11.setCurrentState(contact1.getTitle());
            loadEvents.createEvent(event11, contact1.getTenantUniqueName());

            mongoTemplate.save(contact2, contact2.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
            Event event2 = new Event(contact2.getUser(), contact2.getId(), EventState.CREATED);
            loadEvents.createEvent(event2, contact2.getTenantUniqueName());

            contact2.getTags().add("NewLoad2");
            Event event21 = new Event(contact2.getUser(), contact2.getId(), EventState.TAG_ADD);
            event21.setPropKey("Tags");
            event21.setCurrentState("NewLoad2");
            loadEvents.createEvent(event21, contact2.getTenantUniqueName());

            mongoTemplate.save(contact3, contact3.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
            Event event3 = new Event(contact3.getUser(), contact3.getId(), EventState.CREATED);
            loadEvents.createEvent(event3, contact3.getTenantUniqueName());

            String exProp = contact3.getProps().get(HOBBY);
            contact3.getProps().put(HOBBY, "Photography, Travel");
            Event event31 = new Event(contact3.getUser(), contact3.getId(), EventState.PROP_ADD);
            event31.setPropKey("Props");
            event31.setPrevState(exProp);
            event31.setCurrentState(contact3.getProps().get(HOBBY));
            loadEvents.createEvent(event31, contact3.getTenantUniqueName());

            mongoTemplate.save(contact4, contact4.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());
            Event event4 = new Event(contact4.getUser(), contact4.getId(), EventState.CREATED);
            loadEvents.createEvent(event4, contact4.getTenantUniqueName());

            Contact contact5 = new Contact("", "Contact 5", USER3, tenantUniqueNames[1], "", LocalDateTime.now(), tags4, selectRandomProps(props, 4), "");
            contact5.setId(contact5.generateId(contact5.getTitle()));
            mongoTemplate.save(contact5, contact5.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());

            Event event5 = new Event(contact5.getUser(), contact5.getId(), EventState.DELETED);
            event5.setPrevState(contact5.getId());
            loadEvents.createEvent(event5, contact5.getTenantUniqueName());
            mongoTemplate.remove(contact5, contact5.getTenantUniqueName() + CollectionType.MAIN.getCollectionType());

            log.info("Loaded test Contacts into the database.");

            tenantServices.addTags(contact1.getTenantUniqueName(), contact1.getTags());
            tenantServices.addTags(contact2.getTenantUniqueName(), contact2.getTags());
            tenantServices.addTags(contact3.getTenantUniqueName(), contact3.getTags());
            tenantServices.addTags(contact4.getTenantUniqueName(), contact4.getTags());
            log.info("Added tags from Contacts to tenants into the database.");
        }
    }

    public List<String> selectRandomTags(List<String> tags, int count) {
        if (count >= tags.size()) {
            return new ArrayList<>(tags);
        }

        List<String> randomTags = new ArrayList<>();
        for (int i = count - 1; i >= 0; i--) {
            int randomIndex = random.nextInt(tags.size());
            String randomTag = tags.get(randomIndex);
            randomTags.add(randomTag);
            tags.remove(randomIndex);
        }
        return randomTags;
    }

    public Map<String, String> selectRandomProps(Map<String, String> allProps, int count) {
        Map<String, String> selectedProps = new HashMap<>();
        List<String> allKeys = new ArrayList<>(allProps.keySet());

        for (int i = 0; i < count; i++) {
            if (allKeys.isEmpty()) {
                break;
            }

            int randomIndex = random.nextInt(allKeys.size());
            String randomKey = allKeys.get(randomIndex);
            allKeys.remove(randomIndex);

            if (allProps.containsKey(randomKey)) {
                selectedProps.put(randomKey, allProps.get(randomKey));
            }
        }
        return selectedProps;
    }
}
