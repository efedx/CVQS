package com.security.interfaces;

import com.security.dto.LoginRequestDto;

public interface LoginService {
    String login(LoginRequestDto loginRequestDto);
}
