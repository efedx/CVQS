package com.example.project.security;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
}
