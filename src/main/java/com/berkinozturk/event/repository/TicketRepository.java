package com.berkinozturk.event.repository;

import com.berkinozturk.event.entity.TicketEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<TicketEntity, String> {
}