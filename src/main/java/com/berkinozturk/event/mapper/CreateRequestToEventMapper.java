package com.berkinozturk.event.mapper;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.request.EventCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateRequestToEventMapper {

    public static EventEntity toEventEntity(EventCreateRequest request) {
        return EventEntity.builder()
                .eventName(request.getEventName())
                .eventLocation(request.getEventLocation())
                .eventOwner(request.getEventOwner())
                .eventDate(request.getEventDate())
                .build();
    }
}
