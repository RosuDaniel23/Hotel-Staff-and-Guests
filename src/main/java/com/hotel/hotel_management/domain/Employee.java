package com.hotel.hotel_management.domain;

import jakarta.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String role; // RECEPTIONIST, CLEANER, MANAGER

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=true)
    private String password;

    public Employee() {}

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    // setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

}
