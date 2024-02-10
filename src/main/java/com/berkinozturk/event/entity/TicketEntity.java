package com.berkinozturk.event.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Data
@Getter
@Document
@Builder
public class TicketEntity {

        @Id
        private String id;
        private TicketType ticketType;
        private double ticketPrice;
        private int ticketQuantity;
}
