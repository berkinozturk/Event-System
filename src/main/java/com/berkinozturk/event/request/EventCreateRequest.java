package com.berkinozturk.event.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateRequest {
    private String eventName;
    private String eventLocation;
    private LocalDateTime eventDate;
    private String eventOwner;

}
