package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {
    private String username;
    private String email;
    private String password;
    //@Enumerated(value = EnumType.STRING) // todo make here work with enums
    // private Role role;
    private String[] roles;
}
