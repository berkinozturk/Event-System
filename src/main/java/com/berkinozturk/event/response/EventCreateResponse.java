package com.berkinozturk.event.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateResponse {
    private String eventName;
    private String eventLocation;
    private String eventOwner;
    private String eventDate;

}
