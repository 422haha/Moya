package com.e22e.moya.user.service.jwt;

import java.util.Map;

public interface LoginSuccessService {

    Map<String, String> generateTokens(String email);

    String getSuccessResponseBody();
}
