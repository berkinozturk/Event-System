package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.repository.EventRepository;
import com.berkinozturk.event.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableCaching
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;



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
