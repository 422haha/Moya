package com.e22e.moya.common.oauth2;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final String accessToken;
    private final String registrationId;

    public CustomOAuth2User(OAuth2User oauth2User, String accessToken, String registrationId) {
        this.oauth2User = oauth2User;
        this.accessToken = accessToken;
        this.registrationId = registrationId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        Map<String, Object> attributes = getAttributes();
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return response.get("id").toString();
        } else if ("kakao".equals(registrationId)) {
            return attributes.get("id").toString();
        }
        return oauth2User.getName();
    }

    public String getAccessToken() {
        return accessToken;
    }

}