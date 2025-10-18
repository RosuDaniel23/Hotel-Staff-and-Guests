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

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }

    public static class Update {
        @NotBlank
        private String number;
        @NotBlank
        private String type;
        @NotNull @Positive
        private BigDecimal price;

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }

    public static class Response {
        private Long id;
        private String number;
        private String type;
        private BigDecimal price;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}
