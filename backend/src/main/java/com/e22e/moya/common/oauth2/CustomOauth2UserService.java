package com.e22e.moya.common.oauth2;

import com.e22e.moya.user.service.UserServiceImpl;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService {

    private final UserServiceImpl userService;

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        return (userRequest) -> {
            log.info("OAuth2UserService 호출");
            log.info("ClientRegistration: {}",
                userRequest.getClientRegistration().getRegistrationId());
            log.info("액세스 토큰: {}", userRequest.getAccessToken().getTokenValue());
            OAuth2User oauth2User = defaultOAuth2UserService.loadUser(userRequest);
            log.info("oauth2User: {}", oauth2User);

            // kakao에서 받아오는 사용자 정보
            Map<String, Object> attributes = oauth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get(
                "kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.get("email");
            String name = (String) profile.get("nickname");
            String profileImageUrl = (String) profile.get("profile_image_url");
            String oauthId = oauth2User.getName();
            String oauthProvider = userRequest.getClientRegistration().getRegistrationId();

            userService.findOrCreateUser(email, name, oauthProvider, oauthId,
                profileImageUrl);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();

            return new CustomOAuth2User(oauth2User, accessToken.getTokenValue());
        };
    }
}