package com.e22e.moya.user.service.user;

import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.dto.UserNameResponseDto;

public interface UserService {

    Users findOrCreateUser(String email, String name, String profileImageUrl);

    UserNameResponseDto getUserName(Long userId);

    String getEmailById(Long userId);
}
