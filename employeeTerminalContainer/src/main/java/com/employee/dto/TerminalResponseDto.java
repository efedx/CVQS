package com.employee.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TerminalResponseDto {
    private Long id;
    private String departmentName;
    private String terminalName;
}
