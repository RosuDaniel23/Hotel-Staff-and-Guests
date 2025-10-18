package com.hotel.hotel_management.dto;

import jakarta.validation.constraints.*;

public class EmployeeDtos {

    public static class Create {
        @NotBlank
        private String name;
        @NotBlank
        private String role;
        @NotBlank @Email
        private String email;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class Update {
        @NotBlank
        private String name;
        @NotBlank
        private String role;
        @NotBlank @Email
        private String email;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class Response {
        private Long id;
        private String name;
        private String role;
        private String email;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
