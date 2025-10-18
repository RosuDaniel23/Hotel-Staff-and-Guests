package com.hotel.hotel_management.domain;

import jakarta.persistence.*;

@Entity
public class Guest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, unique=true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER) // <- schimba LAZY in EAGER
    @JoinColumn(name = "room_id")
    private Room room;

    public Guest() {}
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Room getRoom() { return room; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRoom(Room room) { this.room = room; }
}
