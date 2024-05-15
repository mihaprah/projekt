package com.scm.scm.events.rest;

import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import com.scm.scm.support.security.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    private final EventsServices eventsServices;
    private final UserAccessService userAccessService;

    @Autowired
    public EventsController(EventsServices eventsServices, UserAccessService userAccessService) {
        this.eventsServices = eventsServices;
        this.userAccessService = userAccessService;
    }

    @GetMapping("/{contact_id}/{tenant_unique_name}")
    public ResponseEntity<List<Event>> getAllEventsForContact(@PathVariable("contact_id") String contactId, @PathVariable("tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        boolean check = userAccessService.hasAccessToContact(userToken, tenantUniqueName);
        if (!check) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(eventsServices.getAllEventsForContact(contactId, tenantUniqueName));
    }

    @GetMapping("/{tenant_unique_name}")
    public ResponseEntity<List<Event>> getAllEventsForTenant(@PathVariable("tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken, @RequestHeader("tenantId") String tenantId) {
        boolean check = userAccessService.hasAccessToTenant(userToken, tenantId);
        if (!check) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(eventsServices.getAllEventsForTenant(tenantUniqueName));
    }
}
