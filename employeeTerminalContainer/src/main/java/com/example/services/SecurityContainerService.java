package com.example.services;

import com.example.dto.JwtDto;
import com.example.dto.JwtGenerationRequestDto;
import com.example.dto.LoginRequestDto;
import com.example.exceptions.CustomSecurityException;
import com.example.exceptions.SecurityExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecurityContainerService {

    @Autowired
    RestTemplate restTemplate;

    public void jwtValidation(String authorizationHeader, String url) throws JsonProcessingException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<JwtGenerationRequestDto> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<SecurityExceptionResponse> validationResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SecurityExceptionResponse.class);

        if(validationResponse.getStatusCode().isError()) {

            //SecurityExceptionResponse securityExceptionResponse = JsonToSecurityExceptionResponse(validationResponse);
            SecurityExceptionResponse securityExceptionResponse = validationResponse.getBody();
            //HttpStatus httpStatus = HttpStatus.valueOf(validationResponse.getStatusCode().value());
            HttpStatusCode httpStatusCode = validationResponse.getStatusCode();
            String message = securityExceptionResponse.message();
            throw new CustomSecurityException(message, httpStatusCode);
        }
    }

    public ResponseEntity<JwtDto> loginValidation(LoginRequestDto loginRequestDto, String url) {

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequestDto); // first parameter is the body
        ResponseEntity<Object> jwtResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);

        if(jwtResponse.getStatusCode().is2xxSuccessful()) {
            HttpStatusCode statusCode = jwtResponse.getStatusCode();
            HttpHeaders headers = jwtResponse.getHeaders();
            JwtDto jwtResponseBody = (JwtDto) jwtResponse.getBody();
            return new ResponseEntity<>(jwtResponseBody, headers, statusCode);
        }

        else {
            SecurityExceptionResponse securityExceptionResponse = (SecurityExceptionResponse) jwtResponse.getBody();
            HttpStatusCode httpStatusCode = jwtResponse.getStatusCode();
            String message = securityExceptionResponse.message();
            throw new CustomSecurityException(message, httpStatusCode);
        }
    }

    private SecurityExceptionResponse JsonToSecurityExceptionResponse(ResponseEntity<Object> responseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SecurityExceptionResponse securityExceptionResponse = objectMapper.readValue(responseEntity.getBody().toString(), SecurityExceptionResponse.class);
        return securityExceptionResponse;
    }

}
