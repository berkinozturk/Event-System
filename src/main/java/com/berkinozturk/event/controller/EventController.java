package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody EventEntity eventEntity) {
        return ResponseEntity.ok(eventService.saveEvent(eventEntity));
    }

    @GetMapping
    public ResponseEntity<List<EventEntity>> findAll() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.accepted().build();
    }
}
