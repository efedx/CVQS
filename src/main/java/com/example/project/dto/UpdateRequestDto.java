package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestDto {
    private Long id;
    private String username;
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    //@Email(message = "Provide a valid email address")
    private String email;
    private String[] roles;
}
