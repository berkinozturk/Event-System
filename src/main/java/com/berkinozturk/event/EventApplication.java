package com.berkinozturk.event;

import com.berkinozturk.event.entity.EventEntity;
import com.berkinozturk.event.entity.TicketEntity;
import com.berkinozturk.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.berkinozturk.event.entity.TicketType.VIP;

@SpringBootApplication
public class EventApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
	}

	//@Bean
	public CommandLineRunner commandLineRunner(@Autowired EventRepository eventRepository ) {

		return args -> {

			EventEntity event = EventEntity.builder()
					.eventName("Spring Boot Event")
					.eventLocation("Istanbul")
					.eventOwner("Berkin Öztürk")
					.eventTags(List.of("Spring Boot", "Java", "MongoDB"))
					.eventTicketType(TicketEntity.builder()
							.ticketType(VIP)
							.ticketPrice(100.0)
							.ticketQuantity(2)
							.build())
					.build();

			eventRepository.insert(event);

		};
	}

}
