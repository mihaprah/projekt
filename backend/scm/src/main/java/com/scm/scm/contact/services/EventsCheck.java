package com.scm.scm.contact.services;

import com.scm.scm.contact.vao.Contact;
import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.events.vao.EventState;
import com.scm.scm.tenant.services.TenantServices;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventsCheck {
    private final EventsServices eventsServices;

    private final TenantServices tenantServices;

    public EventsCheck(EventsServices eventsServices, TenantServices tenantServices) {
        this.eventsServices = eventsServices;
        this.tenantServices = tenantServices;
    }

    public void checkProps (Contact existingContact, Contact contact){
        Map<String, String> existingProps = existingContact.getProps();
        Map<String, String> props = contact.getProps();

        Event event = new Event();
        event.setUser(contact.getUser());
        event.setContact(existingContact.getId());

        for (Map.Entry<String, String> entry : props.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if(existingProps.containsKey(key)){
                String existingValue = existingProps.get(key);
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
                event.setCurrentState(value);
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());
            }
        }
        for (Map.Entry<String, String> entry : existingProps.entrySet()){
            String key = entry.getKey();
            if (!props.containsKey(key)){
                event.setEventState(EventState.PROP_REMOVED);
                event.setPropKey(key);
                event.setPrevState(entry.getValue());
                event.setCurrentState("");
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());
            }
        }
    }

    public void checkTags (Contact existingContact, Contact contact) {
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
        for (String existingTag : existingTags){
            if(!tags.contains(existingTag)){
                event.setEventState(EventState.TAG_REMOVED);
                event.setPropKey("TAG");
                event.setPrevState(existingTag);
                event.setCurrentState("");
                eventsServices.addEvent(event, existingContact.getTenantUniqueName());

                List<String> removedTags = new ArrayList<>();
                removedTags.add(existingTag);

                tenantServices.removeTags(contact.getTenantUniqueName(), removedTags);
            }
        }
    }
}
