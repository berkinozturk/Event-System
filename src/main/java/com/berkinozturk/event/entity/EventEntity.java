package com.berkinozturk.event.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EventEntity implements Serializable{
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
