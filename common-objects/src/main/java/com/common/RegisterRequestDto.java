package com.common;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    private String username;
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    private String email;
    @NotEmpty(message = "Roles cannot be null or empty")
    private Set<RoleDto> roleSet;

    @Data
    public static class RoleDto {
        private String roleName;
    }
    @Nullable
    private String department;
    @Nullable
    private String terminal;
}