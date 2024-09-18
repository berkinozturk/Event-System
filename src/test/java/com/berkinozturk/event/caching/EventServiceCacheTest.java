package com.berkinozturk.event.caching;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.request.EventCreateRequest;
import com.berkinozturk.event.response.EventCreateResponse;
import com.berkinozturk.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EventServiceCacheTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCacheEviction() {
        String eventId;

        // Create an event
        EventCreateRequest eventCreateRequest = new EventCreateRequest();
        eventCreateRequest.setEventName("Test Event");
        eventCreateRequest.setEventLocation("Test Location");
        eventCreateRequest.setEventDate(LocalDateTime.now());  // Set the event date here

        EventCreateResponse createdEventResponse = eventService.createEvent(eventCreateRequest);
        eventId = createdEventResponse.getId();  // Get the generated ID from the response

        // Fetch the event from the database (should be a cache miss initially)
        EventEntity fetchedEvent = eventService.findById(eventId);
        assertNotNull(fetchedEvent);
        assertEquals("Test Event", fetchedEvent.getEventName());

        // Verify that the cache contains the event
        Cache cache = cacheManager.getCache("events");
        assertNotNull(cache);
        assertNotNull(cache.get(eventId));

        // Update the event
        eventService.updateEvent(eventId, "Updated Event", "Updated Location");

        // Fetch the updated event (should be a cache miss after update and should be re-cached)
        EventEntity updatedEvent = eventService.findById(eventId);
        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getEventName());

        // Verify that the cache contains the updated event
        Cache updatedCache = cacheManager.getCache("events");
        assertNotNull(updatedCache);
        assertNotNull(updatedCache.get(eventId));
    }


}
