package com.example.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestDto {

    @Nullable
    private String username;
    @Nullable
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    @Nullable
    //@Email(message = "Provide a valid email address")
    private String email;
    @Nullable
    private Set<RegisterRequestDto.RoleDto> roleSet;

//    @Data
//    @Nullable
//    public static class RoleDto {
//        private String roleName;
//    }
}
