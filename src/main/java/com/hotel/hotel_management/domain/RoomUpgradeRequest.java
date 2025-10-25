package com.hotel.hotel_management.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RoomUpgradeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_room_id", nullable = false)
    private Room currentRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requested_room_id", nullable = false)
    private Room requestedRoom;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    public RoomUpgradeRequest() {
        this.requestedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Long getId() { return id; }
    public Guest getGuest() { return guest; }
    public Room getCurrentRoom() { return currentRoom; }
    public Room getRequestedRoom() { return requestedRoom; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }

    public void setId(Long id) { this.id = id; }
    public void setGuest(Guest guest) { this.guest = guest; }
    public void setCurrentRoom(Room currentRoom) { this.currentRoom = currentRoom; }
    public void setRequestedRoom(Room requestedRoom) { this.requestedRoom = requestedRoom; }
    public void setStatus(String status) { this.status = status; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}

