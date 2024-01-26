package com.berkinozturk.event.service;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public String saveEvent(EventEntity eventEntity) {
        return eventRepository.save(eventEntity).getId();
    }

    public EventEntity findById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public List<EventEntity> findAllEvents() {
        return eventRepository.findAll();
    }

}
