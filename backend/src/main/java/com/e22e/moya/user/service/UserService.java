package com.e22e.moya.user.service;

import com.e22e.moya.user.dto.UserNameResponseDto;

public interface UserService {

    UserNameResponseDto getUserName(Long userId);
}
