package com.e22e.moya.user.service.oauth;

import com.e22e.moya.common.config.OAuthPropertiesConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.service.user.UserServiceImpl;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuthLoginServiceImpl implements OAuthLoginService {

    private final UserServiceImpl userService;
    private final OAuthPropertiesConfig oAuthPropertiesConfig;

    public OAuthLoginServiceImpl(UserServiceImpl userService,
        OAuthPropertiesConfig oAuthPropertiesConfig) {
        this.userService = userService;
        this.oAuthPropertiesConfig = oAuthPropertiesConfig;
    }

    /**
     * 로그인
     *
     * @param provider    oauth 인증 제공자
     * @param accessToken oauth 액세스 토큰
     * @return 로그인 처리 결과
     */
    @Override
    public Long loginUser(String provider, String accessToken) throws Exception {
        log.info("{} 로그인 시도, access token: {}", provider, accessToken);
        try {
            Map<String, Object> userInfo = getUserInfoFromProvider(provider, accessToken);

            String email, name, profileImageUrl;

            if ("kakao".equals(provider)) {
                log.info("kakao 로그인중");
                Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get(
                    "kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                email = (String) kakaoAccount.get("email");
                name = (String) profile.get("nickname");
                profileImageUrl = (String) profile.get("profile_image_url");
            } else if ("naver".equals(provider)) {
                log.info("naver 로그인중");
                Map<String, Object> response = (Map<String, Object>) userInfo.get("response");

                email = (String) response.get("email");
                name = (String) response.get("nickname");
                profileImageUrl = (String) response.get("profile_image");

                log.debug("추출된 이메일: {}, 이름: {}, 프로필 이미지: {}", email, name, profileImageUrl);
            } else {
                log.error("지원하지 않는 OAuth 제공자 : {}", provider);
                throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
            }

            // User 찾거나 없으면 등록
            Users user = userService.findOrCreateUser(email, name, profileImageUrl);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("사용자 인증 성공: {}", user.getEmail());

            return user.getId();
        } catch (Exception e) {
            log.error("로그인 중 오류 발생: ", e);
            throw e;
        }
    }

    /**
     * OAuth 제공자로부터 사용자 정보 가져옴
     *
     * @param provider    oauth 제공자
     * @param accessToken oauth 액세스 토큰
     * @return 사용자 정보
     */
    private Map getUserInfoFromProvider(String provider, String accessToken)
        throws Exception {
        log.info("{}에서 사용자 정보 가져오는 중", provider);
        // 제공자별 사용자 정보 엔드포인트
        String userInfoEndpoint;
        if ("kakao".equals(provider)) {
            userInfoEndpoint = "https://kapi.kakao.com/v2/user/me";
        } else if ("naver".equals(provider)) {
            userInfoEndpoint = "https://openapi.naver.com/v1/nid/me";
        } else {
            throw new IllegalArgumentException("지원하지 않는 oauth 제공자: " + provider);
        }

        log.debug("사용자 정보 엔드포인트: {}", userInfoEndpoint);

        // http 클라이언트 생성
        HttpClient client = HttpClient.newHttpClient();
        // http get 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(userInfoEndpoint))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        log.debug("사용자 정보를 가져오기 위해 HTTP 요청 전송");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("API 응답: {}", response.body());

        // json응답을 map으로
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), Map.class);
    }
}
