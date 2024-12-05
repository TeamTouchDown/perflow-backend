package com.touchdown.perflowbackend.security.repository;

import com.touchdown.perflowbackend.security.redis.BlackAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface BlackAccessTokenRepository extends CrudRepository<BlackAccessToken, Long> {
}
