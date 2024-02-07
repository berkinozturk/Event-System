package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.entity.RoleType;
import com.berkinozturk.event.entity.UserEntity;
import com.berkinozturk.event.exception.EntityNotFoundException;
import com.berkinozturk.event.exception.UnauthorizedException;
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

    @Cacheable(value = "allEvents")
    public List<EventEntity> findAllEvents() {
        return eventRepository.findAll();
    }

    @CacheEvict(value = "events", allEntries = true)
    public void createEvent(String userId, EventEntity eventEntity) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!hasAdminRole(user.getUsername())) {
            throw new UnauthorizedException("You are not authorized to create events");
        }

        eventRepository.save(eventEntity);
    }
    @CacheEvict(value = "events", key = "#eventId")
    public void updateEventAdmin(String userId, String eventId, EventEntity event) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        EventEntity eventData = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        if (!hasAdminRole(user.getUsername())) {
            throw new UnauthorizedException("You are not authorized to edit events");
        }

        event.setEventName(eventData.getEventName());
        event.setEventLocation(eventData.getEventLocation());
        event.setEventDate(eventData.getEventDate());
        event.setEventOwner(eventData.getEventOwner());
        event.setEventTags(eventData.getEventTags());
        event.setEventTicketType(eventData.getEventTicketType());

        eventRepository.save(event);

    }

    @CacheEvict(value = "events", key = "#id")
    public void deleteEventAdmin(String userId, String eventId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (!hasAdminRole(user.getUsername())) {
            throw new UnauthorizedException("You are not authorized to delete events");
        }

        eventRepository.deleteById(eventId);
    }

    private boolean hasAdminRole(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.map(userEntity -> userEntity.getRole() == RoleType.ADMIN).orElse(false);
    }



}
