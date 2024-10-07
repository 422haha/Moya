package com.e22e.moya.common.config;

import com.e22e.moya.common.filter.JWTTokenValidatorFilter;
import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class ProjectSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public ProjectSecurityConfig(JwtUtil jwtUtil, UserRepository userRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
            .addFilterBefore(new JWTTokenValidatorFilter(jwtUtil, userRepository),
                UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeRequests(authorize -> authorize
                .requestMatchers("/", "/user/login", "/oauth2/**", "/auth/**0", "/error")
                .permitAll()
                .anyRequest().permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

}