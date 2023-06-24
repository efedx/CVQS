package com.example.dto;

import com.example.model.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    //@NotEmpty
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    //@Email(message = "Provide a valid email address")
    @NotBlank
    @Email
    private String email;
    //@Enumerated(value = EnumType.STRING) // todo make here work with enums
    // private Role role;
    //@NotBlank(message = "Roles cannot be null or empty")
    private Set<RoleDto> roleSet;

    @Data
    public static class RoleDto {
        //private Long roleId;
        private String roleName;

    }
}
