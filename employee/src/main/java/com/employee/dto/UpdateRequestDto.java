package com.employee.dto;

import jakarta.annotation.Nullable;
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
public class UpdateRequestDto {

    @Nullable
    private String username;
    @Nullable
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    @Nullable
    private String email;

    @Nullable
    private String department;

    @Nullable
    private String terminal;

    @Nullable
    private Set<RoleDto> roleSet;

    @Data
    public static class RoleDto {
        private String roleName;
    }
}
