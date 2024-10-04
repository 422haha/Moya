package com.e22e.moya.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class OAuthPropertiesConfig {

    private Kakao kakao = new Kakao();
    private Naver naver = new Naver();

    @Getter
    @Setter
    public static class Kakao {

        private String clientId;
        private String clientSecret;
        private String userInfoUri;

    }

    @Getter
    @Setter
    public static class Naver {

        private String clientId;
        private String clientSecret;
        private String userInfoUri;

    }

}
