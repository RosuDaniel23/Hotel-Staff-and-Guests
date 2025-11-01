package com.hotel.hotel_management.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GuestServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @Column(nullable = false)
    private String type;

    @Column(length = 1000)
    private String note;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    public GuestServiceRequest() {
        this.status = "PENDING";
        this.requestedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Guest getGuest() { return guest; }
    public String getType() { return type; }
    public String getNote() { return note; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }

    public void setId(Long id) { this.id = id; }
    public void setGuest(Guest guest) { this.guest = guest; }
    public void setType(String type) { this.type = type; }
    public void setNote(String note) { this.note = note; }
    public void setStatus(String status) { this.status = status; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}

