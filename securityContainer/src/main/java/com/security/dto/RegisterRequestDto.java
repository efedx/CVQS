//package com.security.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Size;
//import java.util.Set;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RegisterRequestDto {
//    private String username;
//    private Set<RoleDto> roleSet;
//
//    @Data
//    public static class RoleDto {
//        private String roleName;
//    }
//}

package com.security.dto;

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
public class RegisterRequestDto {
    private String username;
    @Size(min = 5, message = "Passwords must be at least 5 characters long")
    private String password;
    private String email;
    @NotEmpty(message = "Roles cannot be null or empty")
    private Set<RoleDto> roleSet;

    @Data
    public static class RoleDto {
        private String roleName;
    }
}