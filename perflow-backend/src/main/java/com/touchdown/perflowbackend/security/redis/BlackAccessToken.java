package com.touchdown.perflowbackend.security.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "accessToken", timeToLive = 14440)
public class BlackAccessToken {

    @Id
    private String empId;

    private String accessToken;

    private Long expiration;

    public BlackAccessToken(String accessToken, String empId, Long expiration) {

        this.accessToken = accessToken;
        this.empId = empId;
        this.expiration = expiration;
    }
}
