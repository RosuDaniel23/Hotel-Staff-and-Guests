package com.hotel.hotel_management.dto;

import jakarta.validation.constraints.*;

public class GuestDtos {

    public static class Create {
        @NotBlank
        private String name;
        @NotBlank @Email
        private String email;
        private Long roomId; // opțional la creare

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Long getRoomId() { return roomId; }
        public void setRoomId(Long roomId) { this.roomId = roomId; }
    }

    public static class Update {
        @NotBlank
        private String name;
        @NotBlank @Email
        private String email;
        private Long roomId; // poate fi null

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Long getRoomId() { return roomId; }
        public void setRoomId(Long roomId) { this.roomId = roomId; }
    }

    public static class Response {
        private Long id;
        private String name;
        private String email;
        private Long roomId;
        private String roomNumber;
        private String roomType;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Long getRoomId() { return roomId; }
        public void setRoomId(Long roomId) { this.roomId = roomId; }
        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
        public String getRoomType() { return roomType; }
        public void setRoomType(String roomType) { this.roomType = roomType; }
    }
}
