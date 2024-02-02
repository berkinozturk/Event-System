package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.repository.EventRepository;
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

    public String saveEvent(EventEntity eventEntity) {
        return eventRepository.save(eventEntity).getId();
    }
    @Cacheable(value = "events", key = "#id")
    public EventEntity findById(String id) {
        return eventRepository.findById(id).orElse(null);
    }
    @CacheEvict(value = "events", key = "#id")
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }
    @Cacheable(value = "allEvents")
    public List<EventEntity> findAllEvents() {
        return eventRepository.findAll();
    }

    public EventEntity updateEvent(String id, EventEntity updatedEvent) {
        EventEntity existingEvent = findById(id);
        return eventRepository.save(existingEvent);
    }

}
