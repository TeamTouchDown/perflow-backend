package com.touchdown.perflowbackend.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidity;

    public JwtTokenProvider(@Value("${token.secret}") String secret,
                         @Value("${token.access_token_expiration_time}") long accessTokenValidity) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidity = accessTokenValidity;
    }

    // Access Token 생성 메서드
    public String createAccessToken(String subject, Map<String, Object> claims) {

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 사용자 식별자
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(now.getTime() + accessTokenValidity)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘
                .compact();
    }
}
