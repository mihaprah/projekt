package com.scm.scm.events;

import com.scm.scm.events.services.EventsServices;
import com.scm.scm.events.vao.Event;
import com.scm.scm.support.security.UserAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventsServices eventsServices;

    @MockBean
    private UserAccessService userAccessService;

    private List<Event> eventList;

    @BeforeEach
    public void setUp() {
        eventList = Collections.singletonList(new Event());
    }

    @Test
    public void getAllEventsForContactTest() throws Exception {
        when(userAccessService.hasAccessToContact(anyString(), anyString())).thenReturn(true);
        when(eventsServices.getAllEventsForContact(anyString(), anyString())).thenReturn(eventList);

        mockMvc.perform(get("/events/contact_id/tenant_unique_name")
                        .header("userToken", "token"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllEventsForTenantTest() throws Exception {
        when(userAccessService.hasAccessToTenant(anyString(), anyString())).thenReturn(true);
        when(eventsServices.getAllEventsForTenant(anyString())).thenReturn(eventList);

        mockMvc.perform(get("/events/tenant_unique_name")
                        .header("userToken", "token")
                        .header("tenantId", "tenantId"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllEventsForContactShouldThrowExceptionWhenUserHasNoAccess() throws Exception {
        when(userAccessService.hasAccessToContact(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(get("/events/contact_id/tenant_unique_name")
                        .header("userToken", "token"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllEventsForTenantShouldThrowExceptionWhenUserHasNoAccess() throws Exception {
        when(userAccessService.hasAccessToTenant(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(get("/events/tenant_unique_name")
                        .header("userToken", "token")
                        .header("tenantId", "tenantId"))
                .andExpect(status().isForbidden());
    }
}