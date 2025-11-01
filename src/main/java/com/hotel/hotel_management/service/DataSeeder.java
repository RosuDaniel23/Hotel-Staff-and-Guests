package com.hotel.hotel_management.service;

import com.hotel.hotel_management.domain.Employee;
import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.domain.Room;
import com.hotel.hotel_management.repository.EmployeeRepository;
import com.hotel.hotel_management.repository.GuestRepository;
import com.hotel.hotel_management.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public DataSeeder(EmployeeRepository employeeRepository,
                      GuestRepository guestRepository,
                      RoomRepository roomRepository) {
        this.employeeRepository = employeeRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Room room201 = roomRepository.findByNumber("201").orElseGet(() -> {
            Room r = new Room();
            r.setNumber("201");
            r.setType("Double");
            r.setPrice(new BigDecimal("120.00"));
            r.setStatus("BOOKED");
            return roomRepository.save(r);
        });

        Room room301 = roomRepository.findByNumber("301").orElseGet(() -> {
            Room r = new Room();
            r.setNumber("301");
            r.setType("Suite");
            r.setPrice(new BigDecimal("250.00"));
            r.setStatus("BOOKED");
            return roomRepository.save(r);
        });

        Room room302 = roomRepository.findByNumber("302").orElseGet(() -> {
            Room r = new Room();
            r.setNumber("302");
            r.setType("Suite");
            r.setPrice(new BigDecimal("300.00"));
            r.setStatus("AVAILABLE");
            return roomRepository.save(r);
        });

        Room room402 = roomRepository.findByNumber("402").orElseGet(() -> {
            Room r = new Room();
            r.setNumber("402");
            r.setType("Deluxe");
            r.setPrice(new BigDecimal("350.00"));
            r.setStatus("AVAILABLE");
            return roomRepository.save(r);
        });

        Employee admin = employeeRepository.findByEmail("admin@hotel.com").orElseGet(() -> {
            Employee e = new Employee();
            e.setName("Admin User");
            e.setEmail("admin@hotel.com");
            e.setRole("MANAGER");
            e.setPassword("password");
            return employeeRepository.save(e);
        });
        if (admin.getPassword() == null || !"password".equals(admin.getPassword())) {
            admin.setPassword("password");
            employeeRepository.save(admin);
        }

        Guest guest = guestRepository.findByEmail("guest@hotel.com").orElseGet(() -> {
            Guest g = new Guest();
            g.setName("Test Guest");
            g.setEmail("guest@hotel.com");
            g.setPassword("password");
            g.setRoom(room201);
            return guestRepository.save(g);
        });
        boolean changed = false;
        if (guest.getPassword() == null || !"password".equals(guest.getPassword())) {
            guest.setPassword("password");
            changed = true;
        }
        if (guest.getRoom() == null) {
            guest.setRoom(room201);
            changed = true;
        }
        if (changed) guestRepository.save(guest);
    }
}
