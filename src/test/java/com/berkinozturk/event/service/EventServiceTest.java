package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindById() {
        String eventId = "eventId";
        EventEntity event = new EventEntity("1", "Event", "Location", LocalDateTime.now(), "Owner", new ArrayList<>(), null);
        event.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        EventEntity foundEvent = eventService.findById(eventId);

        assertEquals(event, foundEvent);
    }



    @Test
    void testFindAllEvents() {
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity("1", "Event1", "Location1", LocalDateTime.now(), "Owner1", new ArrayList<>(), null));
        events.add(new EventEntity("2", "Event2", "Location2", LocalDateTime.now(), "Owner2", new ArrayList<>(), null));

        when(eventRepository.findAll()).thenReturn(events);

        List<EventEntity> foundEvents = eventService.findAllEvents();

        assertEquals(events.size(), foundEvents.size());
        assertTrue(foundEvents.containsAll(events));
    }

    @Test
    void testCreateEvent() {
        EventEntity event = new EventEntity("1", "Event", "Location", LocalDateTime.now(), "Owner", new ArrayList<>(), null);

        eventService.createEvent(event);

        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testUpdateEvent() {
        String eventId = "eventId";
        EventEntity existingEvent = new EventEntity("1", "Event", "Location", LocalDateTime.now(), "Owner", new ArrayList<>(), null);
        existingEvent.setId(eventId);

        EventEntity updatedEvent = new EventEntity("1", "Updated Event", "Updated Location", LocalDateTime.now(), "Updated Owner", new ArrayList<>(), null);
        updatedEvent.setId(eventId);
        updatedEvent.setEventName("Updated Event");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(updatedEvent)).thenReturn(updatedEvent);

        eventService.updateEvent(eventId, updatedEvent);

        verify(eventRepository, times(1)).save(updatedEvent);
    }

    @Test
    void testUpdateEvent_NotFound() {
        String eventId = "nonExistingId";
        EventEntity updatedEvent = new EventEntity("123", "Updated Event", "Updated Location", LocalDateTime.now(), "Updated Owner", new ArrayList<>(), null);

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            eventService.updateEvent(eventId, updatedEvent);
        });
    }


    @Test
    void testDeleteEvent() {
        String eventId = "eventId";

        eventService.deleteEvent(eventId);

        verify(eventRepository, times(1)).deleteById(eventId);
    }
}
