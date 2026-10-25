package com.diego.tiendabackend.dto;

public class AuthResponse {
    public String token;
    public String name;
    public String role;

    public AuthResponse(String token, String name, String role) {
        this.token = token;
        this.name = name;
        this.role = role;
    }
}