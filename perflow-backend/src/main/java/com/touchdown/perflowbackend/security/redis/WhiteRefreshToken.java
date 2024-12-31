package com.touchdown.perflowbackend.security.redis;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@RedisHash(value = "refreshToken", timeToLive = 14440)
public class WhiteRefreshToken {

    @Id
    private String empId;

    private String refreshToken;

    private Long expiration;

    public WhiteRefreshToken(String refreshToken, String empId, Long expiration) {

        this.refreshToken = refreshToken;
        this.empId = empId;
        this.expiration = expiration;
    }
}
