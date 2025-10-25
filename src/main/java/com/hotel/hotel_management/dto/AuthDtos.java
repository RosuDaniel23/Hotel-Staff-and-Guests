package com.hotel.hotel_management.dto;

public class AuthDtos {

    public record LoginRequest(String email, String password) {}

    public record LoginResponse(Long id, String name, String email, String role, String type) {}

    public record GuestLoginResponse(Long id, String name, String email, RoomInfo room) {}

    public record RoomInfo(Long id, String number, String type, String price, String status) {}
}

