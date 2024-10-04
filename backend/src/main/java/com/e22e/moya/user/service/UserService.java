package com.e22e.moya.user.service;

import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.dto.UserNameResponseDto;

public interface UserService {

    Users findOrCreateUser(String email, String name, String oauthProvider, String oauthId,
        String profileImageUrl);

    UserNameResponseDto getUserName(Long userId);
}
