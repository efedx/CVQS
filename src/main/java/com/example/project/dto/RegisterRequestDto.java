package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    //@NotEmpty
    private String username;
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    //@Email(message = "Provide a valid email address")
    private String email;
    //@Enumerated(value = EnumType.STRING) // todo make here work with enums
    // private Role role;
    @NotEmpty(message = "Roles cannot be null or empty")
    private String[] roles;
}
