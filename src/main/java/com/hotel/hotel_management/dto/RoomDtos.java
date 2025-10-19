package com.hotel.hotel_management.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class RoomDtos {

    public static class Create {
        @NotBlank
        private String number;
        @NotBlank
        private String type;
        @NotNull @Positive
        private BigDecimal price;
        private String status = "AVAILABLE"; // Default to AVAILABLE

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Update {
        @NotBlank
        private String number;
        @NotBlank
        private String type;
        @NotNull @Positive
        private BigDecimal price;
        private String status;

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Response {
        private Long id;
        private String number;
        private String type;
        private BigDecimal price;
        private String status;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
