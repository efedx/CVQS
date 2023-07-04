package com.employee.dto;

import jakarta.annotation.Nullable;
import lombok.*;

import java.util.ArrayList;

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
