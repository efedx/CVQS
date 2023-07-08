package com.security.interfaces;

import com.common.LoginRequestDto;

public interface LoginService {
    String login(LoginRequestDto loginRequestDto);
}
