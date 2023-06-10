package com.example.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {
    private String token;
    private List<String> tokenList;
}