package com.berkinozturk.event.controller;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventEntity>> findAll() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<EventEntity> createEvent(@RequestBody EventEntity eventEntity) {
        eventService.createEvent(eventEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable String id, @RequestBody EventEntity event) {
        eventService.updateEvent(id, event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
