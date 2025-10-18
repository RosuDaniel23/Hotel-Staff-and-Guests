package com.hotel.hotel_management.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String number;

    @Column(nullable=false)
    private String type;

    @Column(nullable=false, precision = 10, scale = 2)
    private BigDecimal price;

    public Room() {}
    public Long getId() { return id; }
    public String getNumber() { return number; }
    public String getType() { return type; }
    public BigDecimal getPrice() { return price; }
    public void setId(Long id) { this.id = id; }
    public void setNumber(String number) { this.number = number; }
    public void setType(String type) { this.type = type; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
