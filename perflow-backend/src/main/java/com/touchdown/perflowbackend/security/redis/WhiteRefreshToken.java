package com.touchdown.perflowbackend.security.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 14440)
public class WhiteRefreshToken {

    @Id
    private String refreshToken;
    private String empId;

    public WhiteRefreshToken(String refreshToken, String empId) {
        this.refreshToken = refreshToken;
        this.empId = empId;
    }
}
