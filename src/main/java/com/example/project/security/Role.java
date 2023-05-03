package com.example.project.security;

public enum Role {
    USER("user"),
    ADMIN("admin");
    private String role;

    private Role(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}