package com.terminal.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerminalResponseDto {
    private Long id;
    private String departmentName;
    private String terminalName;
}
