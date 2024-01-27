package com.berkinozturk.event;

import com.berkinozturk.event.entity.CategoryEntity;
import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.repository.CategoryRepository;
import com.berkinozturk.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EventApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
	}

	//@Bean
	public CommandLineRunner commandLineRunner(@Autowired EventRepository eventRepository, CategoryRepository categoryRepository) {

		return args -> {

			CategoryEntity category = CategoryEntity.builder()
					.categoryName("Spring Boot Category")
					.categoryDescription("Spring Boot Category")
					.build();

			EventEntity event = EventEntity.builder()
					.eventName("Spring Boot Event")
					.eventLocation("Spring Boot Event")
					.build();

			categoryRepository.insert(category);

			//eventRepository.insert(event);
		};
	}

}
