package com.e22e.moya.common.oauth2;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private OAuth2User oauth2User;
    private String accessToken;

    public CustomOAuth2User(OAuth2User oauth2User, String accessToken) {
        this.oauth2User = oauth2User;
        this.accessToken = accessToken;
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
        return oauth2User.getAttribute("id").toString();
    }

    public String getAccessToken() {
        return accessToken;
    }

}