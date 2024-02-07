package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventEntity>> findAll() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventEntity eventEntity,
                                                   @RequestHeader("Authorization") String userId) {
        eventService.createEvent(userId, eventEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventEntity> updateEventAdmin(@PathVariable String eventId,
                                                        @RequestBody EventEntity event,
                                                        @RequestHeader("Authorization") String userId) {
        eventService.updateEventAdmin(userId, eventId, event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEventAdmin(@PathVariable String eventId,
                                                 @RequestHeader("Authorization") String userId) {
        eventService.deleteEventAdmin(userId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
