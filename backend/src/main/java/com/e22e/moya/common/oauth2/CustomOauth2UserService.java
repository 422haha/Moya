package com.e22e.moya.common.oauth2;

import com.e22e.moya.user.service.UserServiceImpl;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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

            OAuth2User oauth2User = defaultOAuth2UserService.loadUser(userRequest);
            log.info("OAuth2UserService 호출");
            log.info("client registration: {}",
                userRequest.getClientRegistration().getRegistrationId());
            log.info("액세스 토큰: {}", userRequest.getAccessToken().getTokenValue());
            log.info("oauth2User: {}", oauth2User);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            Map<String, Object> attributes = oauth2User.getAttributes();

            String email, name, profileImageUrl, oauthId;

            if ("kakao".equals(registrationId)) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get(
                    "kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                email = (String) kakaoAccount.get("email");
                name = (String) profile.get("nickname");
                profileImageUrl = (String) profile.get("profile_image_url");
                oauthId = oauth2User.getName();
            } else if ("naver".equals(registrationId)) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");

                email = (String) response.get("email");
                name = (String) response.get("nickname");
                profileImageUrl = (String) response.get("profile_image");
                oauthId = (String) response.get("id");
            } else {
                throw new OAuth2AuthenticationException("OAuth2 제공자가 존재하지 않음: " + registrationId);
            }

            userService.findOrCreateUser(email, name, registrationId, oauthId, profileImageUrl);

            return new CustomOAuth2User(oauth2User, userRequest.getAccessToken().getTokenValue(), registrationId);
        };
    }
}
