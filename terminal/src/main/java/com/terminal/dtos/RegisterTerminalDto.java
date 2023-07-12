package com.terminal.dtos;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTerminalDto {

    private String departmentName;
    private List<TerminalDto> terminalList;
    @Data
    public static class TerminalDto {
        private String terminalName;
        @Nullable
        private Boolean isActive;
    }
}
