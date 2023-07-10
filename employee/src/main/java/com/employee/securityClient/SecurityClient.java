package com.employee.securityClient;

import com.employee.dto.JwtDto;
import com.employee.dto.LoginRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "security"
)
public interface SecurityClient {

    @PostMapping("/login") // NO AUTHORIZATION
    public ResponseEntity<JwtDto> login(@RequestBody LoginRequestDto loginRequestDto);

    @PostMapping("/userManagement") // ADMIN
    public ResponseEntity<Boolean> userManagement(@RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/defects") // OPERATOR
    public ResponseEntity<Boolean> defects(@RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/listDefects") // LEADER
    public ResponseEntity<Boolean> listDefects(@RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/terminals") // ALL
    public ResponseEntity<Boolean> terminals(@RequestHeader("Authorization") String authorizationHeader);

}
