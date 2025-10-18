package com.hotel.hotel_management;

import com.hotel.hotel_management.domain.Employee;
import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.repository.EmployeeRepository;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class HotelManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(HotelManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner seed(RoomRepository rooms, EmployeeRepository employees, GuestRepository guests) {
		return args -> {
			if (rooms.count() == 0) {
				Room r1 = new Room(); r1.setNumber("101"); r1.setType("SINGLE"); r1.setPrice(new BigDecimal("250"));
				Room r2 = new Room(); r2.setNumber("102"); r2.setType("DOUBLE"); r2.setPrice(new BigDecimal("380"));
				r1 = rooms.save(r1);
				r2 = rooms.save(r2);

				Employee e1 = new Employee(); e1.setName("Ana Pop"); e1.setRole("RECEPTIONIST"); e1.setEmail("ana@hotel.ro");
				Employee e2 = new Employee(); e2.setName("Mihai Ionescu"); e2.setRole("CLEANER"); e2.setEmail("mihai@hotel.ro");
				employees.save(e1); employees.save(e2);

				Guest g1 = new Guest(); g1.setName("John Doe"); g1.setEmail("john@example.com"); g1.setRoom(r1);
				Guest g2 = new Guest(); g2.setName("Jane Roe"); g2.setEmail("jane@example.com"); g2.setRoom(r2);
				guests.save(g1); guests.save(g2);
			}
		};
	}
}
