package com.touchdown.perflowbackend.security.repository;

import com.touchdown.perflowbackend.security.redis.WhiteRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface WhiteRefreshTokenRepository extends CrudRepository<WhiteRefreshToken, String> {
}
