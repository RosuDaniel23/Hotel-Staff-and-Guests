package com.hotel.hotel_management.web;

import com.hotel.hotel_management.domain.Employee;
import com.hotel.hotel_management.domain.Guest;
import com.hotel.hotel_management.dto.AuthDtos;
import com.hotel.hotel_management.repository.EmployeeRepository;
import com.hotel.hotel_management.repository.GuestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;

    public AuthController(EmployeeRepository employeeRepository, GuestRepository guestRepository) {
        this.employeeRepository = employeeRepository;
        this.guestRepository = guestRepository;
    }

    @PostMapping("/employee/login")
    public ResponseEntity<?> employeeLogin(@RequestBody AuthDtos.LoginRequest request) {
        Employee employee = employeeRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (employee.getPassword() == null || !employee.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid credentials");
        }

        AuthDtos.LoginResponse response = new AuthDtos.LoginResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRole(),
                "employee"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/guest/login")
    public ResponseEntity<?> guestLogin(@RequestBody AuthDtos.LoginRequest request) {
        Guest guest = guestRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (guest.getPassword() == null || !guest.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid credentials");
        }

        AuthDtos.RoomInfo roomInfo = null;
        if (guest.getRoom() != null) {
            roomInfo = new AuthDtos.RoomInfo(
                    guest.getRoom().getId(),
                    guest.getRoom().getNumber(),
                    guest.getRoom().getType(),
                    guest.getRoom().getPrice().toString(),
                    guest.getRoom().getStatus()
            );
        }

        AuthDtos.GuestLoginResponse response = new AuthDtos.GuestLoginResponse(
                guest.getId(),
                guest.getName(),
                guest.getEmail(),
                roomInfo
        );

        return ResponseEntity.ok(response);
    }
}
