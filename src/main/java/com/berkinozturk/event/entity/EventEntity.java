package com.berkinozturk.event.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EventEntity {
    @Id
    private String id;
    private String eventName;
    private String eventLocation;
    private LocalDateTime eventDate;
    private String eventOwner;
    private List<String> eventTags;

    @DBRef
    private TicketEntity eventTicketType;


}
