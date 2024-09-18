package com.berkinozturk.event.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateResponse {
    private String id;
    private String eventName;
    private String eventLocation;
    private String eventOwner;
    private LocalDateTime eventDate;

}
