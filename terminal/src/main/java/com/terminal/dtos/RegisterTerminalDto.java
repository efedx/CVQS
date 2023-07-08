package com.terminal.dtos;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTerminalDto {

    private String departmentName;
    private ArrayList<TerminalDto> terminalList;
    @Data
    public static class TerminalDto {
        private String terminalName;
        @Nullable
        private Boolean isActive;
    }
}
