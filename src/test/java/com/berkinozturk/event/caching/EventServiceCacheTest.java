package com.berkinozturk.event.caching;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
        String eventId = "testEventId";
        EventEntity event = new EventEntity();
        event.setId(eventId);
        event.setEventName("Test Event");

        // First save the event
        eventService.createEvent(event);

        // Fetch from cache (should be a cache miss)
        EventEntity fetchedEvent = eventService.findById(eventId);
        assertNotNull(fetchedEvent);
        assertEquals("Test Event", fetchedEvent.getEventName());

        // Verify that the cache contains the event
        Cache cache = cacheManager.getCache("events");
        assertNotNull(cache);
        assertNotNull(cache.get(eventId));

        // Update the event
        event.setEventName("Updated Event");
        eventService.updateEvent(eventId, event);

        // Fetch again (should be a cache miss after update and should be re-cached)
        EventEntity updatedEvent = eventService.findById(eventId);
        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getEventName());

        // Verify that the cache contains the updated event
        Cache updatedCache = cacheManager.getCache("events");
        assertNotNull(updatedCache);
        assertNotNull(updatedCache.get(eventId));
    }
}
