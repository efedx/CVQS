package com.example.project.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponseDto {
    private String token;
    private List<String> tokenList;
}
