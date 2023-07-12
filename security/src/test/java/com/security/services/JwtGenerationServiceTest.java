//package com.security.services;
//
//import com.security.dtos.LoginRequestDto;
//import com.security.entities.Employee;
//import com.security.entities.Roles;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.crypto.SecretKey;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class JwtGenerationServiceTest {
//
//    @Mock
//    SecretKey signingKey;
//
//    @InjectMocks
//    JwtGenerationService jwtGenerationService;
//
//    @Test
//    void generateJwt() {
//
//        // given
//        String username = "username";
//
//        Roles role = Roles.builder()
//                .roleName("ROLE_ADMIN")
//                .build();
//        Set<Roles> roleSet = Set.of(role);
//
//        // when
//        when(signingKey.getAlgorithm()).thenReturn(SignatureAlgorithm.HS256.getJcaName());
//        jwtGenerationService.generateJwt(username, roleSet);
//
//        // then
//
//    }
//}