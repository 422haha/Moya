package com.e22e.moya.common.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class OAuthPropertiesConfig implements InitializingBean {

    private Kakao kakao = new Kakao();
    private Naver naver = new Naver();

    @Override
    public void afterPropertiesSet() {
        log.info("초기화 확인:");
        log.info("Kakao - clientId: {}, userInfoUri: {}", kakao.getClientId(), kakao.getUserInfoUri());
        log.info("Naver - clientId: {}, userInfoUri: {}", naver.getClientId(), naver.getUserInfoUri());
    }

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
