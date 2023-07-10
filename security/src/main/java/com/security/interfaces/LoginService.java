package com.security.interfaces;

import com.security.dtos.LoginRequestDto;

public interface LoginService {
    String login(LoginRequestDto loginRequestDto);
}
