package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.request.EventCreateRequest;
import com.berkinozturk.event.request.EventUpdateRequest;
import com.berkinozturk.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAll() {
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity("1", "Event1", "Location1", null, "Owner1", new ArrayList<>(), null));
        events.add(new EventEntity("2", "Event2", "Location2", null, "Owner2", new ArrayList<>(), null));

        when(eventService.findAllEvents()).thenReturn(events);

        ResponseEntity<List<EventEntity>> response = eventController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(events, response.getBody());
    }

    @Test
    void testFindById() {
        String eventId = "eventId";
        EventEntity event = new EventEntity("1", "Event", "Location", null, "Owner", new ArrayList<>(), null);

        when(eventService.findById(eventId)).thenReturn(event);

        ResponseEntity<EventEntity> response = eventController.findById(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void testCreateEvent() {
        EventCreateRequest event = new EventCreateRequest();

        ResponseEntity<EventEntity> response = eventController.createEvent(event);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(eventService, times(1)).createEvent(event);
    }

    @Test
    void testUpdateEvent() {
        String eventId = "eventId";
        EventUpdateRequest event = new EventUpdateRequest();

        ResponseEntity<EventEntity> response = eventController.updateEvent(eventId, event);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testDeleteEvent() {
        String eventId = "eventId";

        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).deleteEvent(eventId);
    }
}
