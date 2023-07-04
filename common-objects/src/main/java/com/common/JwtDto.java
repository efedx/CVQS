package com.common;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {
    private String username;
    private String token;
    private List<String> tokenList;
}
