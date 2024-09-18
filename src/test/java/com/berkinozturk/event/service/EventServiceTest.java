package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.repository.EventRepository;
import com.berkinozturk.event.request.EventCreateRequest;
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
    void testUpdateEvent() {
        // Arrange
        String eventId = "eventId";
        String updatedEventName = "Updated Event";
        String updatedEventLocation = "Updated Location";

        EventEntity existingEvent = new EventEntity("1", "Event", "Location", LocalDateTime.now(), "Owner", new ArrayList<>(), null);
        existingEvent.setId(eventId);

        EventEntity updatedEvent = new EventEntity("1", updatedEventName, updatedEventLocation, LocalDateTime.now(), "Updated Owner", new ArrayList<>(), null);
        updatedEvent.setId(eventId);

        // Mock repository behavior
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(updatedEvent);

        // Act
        eventService.updateEvent(eventId, updatedEventName, updatedEventLocation);

        // Assert
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(any(EventEntity.class));

        // Additional assertions to verify correct updates
        assertEquals(updatedEventName, updatedEvent.getEventName());
        assertEquals(updatedEventLocation, updatedEvent.getEventLocation());
    }


    @Test
    void testUpdateEvent_NotFound() {
        // Arrange
        String eventId = "nonExistingId";
        String updatedEventName = "Updated Event";
        String updatedEventLocation = "Updated Location";

        // Mock repository behavior for non-existing event
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.updateEvent(eventId, updatedEventName, updatedEventLocation);
        });

        // Verify that save method was never called
        verify(eventRepository, never()).save(any(EventEntity.class));
    }



    @Test
    void testDeleteEvent() {
        String eventId = "eventId";

        eventService.deleteEvent(eventId);

        verify(eventRepository, times(1)).deleteById(eventId);
    }
}
