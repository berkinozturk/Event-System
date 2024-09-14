package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.repository.EventRepository;
import com.berkinozturk.event.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableCaching
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // I am not sure but does caching even work here?
    // How do you know that the value is fetched from Redis if it is in Redis before you fetch it from the database?

    // ANSWER ==>
    // The caching is working here, because I controlled it with Redis CLI. I can see the values in the cache.
    // When I typed KEYS * in the Redis CLI, I can see the keys of the cache. And when I typed GET <key>, I can see the values of the cache.
    // After the TTL duration, the keys are deleted from the cache. So, the caching is working here.
    @Cacheable(value = "events", key = "#id")
    public EventEntity findById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "allEvents", key="'all'")
    public List<EventEntity> findAllEvents() {
        return eventRepository.findAll();
    }

    @CacheEvict(value = "events", allEntries = true)
    public void createEvent(EventEntity eventEntity) {
        eventRepository.save(eventEntity);
    }
    @CacheEvict(value = "events", key = "#id")
    public void updateEvent(String id, EventEntity updatedEvent) {

        EventEntity eventData = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));


        eventData.setEventName(updatedEvent.getEventName());
        eventData.setEventLocation(updatedEvent.getEventLocation());
        eventData.setEventDate(updatedEvent.getEventDate());
        eventData.setEventOwner(updatedEvent.getEventOwner());
        eventData.setEventTags(updatedEvent.getEventTags());
        eventData.setEventTicketType(updatedEvent.getEventTicketType());

        eventRepository.save(eventData);

    }
    @CacheEvict(value = "events", key = "#id")
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }




}
