package com.berkinozturk.event.integration;

import com.berkinozturk.event.request.AuthenticationRequest;
import com.berkinozturk.event.request.EventCreateRequest;
import com.berkinozturk.event.response.AuthenticationResponse;
import com.berkinozturk.event.response.EventCreateResponse;
import com.berkinozturk.event.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventIntegrationTest {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAuthenticateAndCreateEvent() {
        AuthenticationRequest authRequest = new AuthenticationRequest("testtest", "testtest");
        AuthenticationResponse authResponse = authService.authenticate(authRequest);

        String token = authResponse.getToken();

        EventCreateRequest eventRequest = EventCreateRequest.builder()
                .eventName("Sample Event")
                .eventLocation("New York")
                .eventDate(LocalDateTime.now().plusDays(1))
                .eventOwner("testuser")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<EventCreateResponse> createResponse = restTemplate.exchange(
                "/api/v1/events/create",
                HttpMethod.POST,
                new HttpEntity<>(eventRequest, headers),
                EventCreateResponse.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    }
}
