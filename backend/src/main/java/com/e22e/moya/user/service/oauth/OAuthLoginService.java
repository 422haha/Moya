package com.e22e.moya.user.service.oauth;

import java.util.Map;

public interface OAuthLoginService {

    Long loginUser(String provider, String accessToken) throws Exception;
}
