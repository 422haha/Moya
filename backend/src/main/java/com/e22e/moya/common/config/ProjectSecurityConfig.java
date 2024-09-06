package com.e22e.moya.common.config;

import com.e22e.moya.common.filter.JWTTokenValidatorFilter;
import com.e22e.moya.common.handler.CustomAuthenticationSuccessHandler;
import com.e22e.moya.common.oauth2.CustomOauth2UserService;
import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class ProjectSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CustomOauth2UserService customOauth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public ProjectSecurityConfig(JwtUtil jwtUtil, UserRepository userRepository,
        CustomOauth2UserService customOauth2UserService,
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.customOauth2UserService = customOauth2UserService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(new JWTTokenValidatorFilter(jwtUtil, userRepository),
                UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .authorizeRequests(authorize -> authorize
                .requestMatchers("/", "/login", "/oauth2/**", "/auth/**0", "/error")
                .permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOauth2UserService.oauth2UserService())
                )
                .successHandler(customAuthenticationSuccessHandler)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

}