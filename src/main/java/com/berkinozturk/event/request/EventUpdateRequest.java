package com.berkinozturk.event.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateRequest {

    private String eventName;
    private String eventLocation;
}
