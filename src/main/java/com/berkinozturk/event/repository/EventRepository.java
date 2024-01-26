package com.berkinozturk.event.repository;

import com.berkinozturk.event.entity.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {
}
