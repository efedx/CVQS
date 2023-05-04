package com.example.project.security;

public enum RoleEnum {
    OPERATOR("operator"), ADMIN("admin"), TEAMLEADER("team leader");

    private String role;

    private RoleEnum(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}