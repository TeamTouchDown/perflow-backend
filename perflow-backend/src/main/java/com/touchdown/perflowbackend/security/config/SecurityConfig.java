package com.touchdown.perflowbackend.security.config;

import com.touchdown.perflowbackend.security.filter.JwtFilter;
import com.touchdown.perflowbackend.security.handler.JwtAccessDeniedHandler;
import com.touchdown.perflowbackend.security.handler.JwtAuthenticationEntryPoint;
import com.touchdown.perflowbackend.security.service.CustomEmployeeDetailsService;
import com.touchdown.perflowbackend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomEmployeeDetailsService employeeDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz ->
                        authz.requestMatchers(new AntPathRequestMatcher("/api/v1/**")).permitAll()
                                .anyRequest().authenticated()
                )
                /* session 로그인 방식을 사용하지 않음 (JWT Token 방식을 사용할 예정) */
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        /* 커스텀 로그인 필터 이전에 JWT 토큰 확인 필터를 설정 */
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        /* 인증, 인가 실패 핸들러 설정 */
        http.exceptionHandling(
                exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                }
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
