package com.scm.scm.events.rest;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import com.scm.scm.support.security.UserAccessService;
import com.scm.scm.support.security.UserVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    private final EventsServices eventsServices;
    private final UserAccessService userAccessService;
    private final UserVerifyService userVerifyService;

    @Autowired
    public EventsController(EventsServices eventsServices, UserAccessService userAccessService, UserVerifyService userVerifyService) {
        this.eventsServices = eventsServices;
        this.userAccessService = userAccessService;
        this.userVerifyService = userVerifyService;
    }

    @GetMapping(value = "/{contact_id}/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getAllEventsForContact(@PathVariable("contact_id") String contactId, @PathVariable("tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToContact(decodedToken.getEmail(), tenantUniqueName)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(eventsServices.getAllEventsForContact(contactId, tenantUniqueName));
    }

    @GetMapping(value = "/{tenant_unique_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getAllEventsForTenant(@PathVariable("tenant_unique_name") String tenantUniqueName, @RequestHeader("userToken") String userToken, @RequestHeader("tenantId") String tenantId) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        if (!userAccessService.hasAccessToTenant(decodedToken.getEmail(), tenantId)) {
            throw new CustomHttpException(ExceptionMessage.USER_ACCESS_TENANT.getExceptionMessage(), 403, ExceptionCause.USER_ERROR);
        }
        return ResponseEntity.ok(eventsServices.getAllEventsForTenant(tenantUniqueName));
    }
}
