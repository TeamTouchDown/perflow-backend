package com.touchdown.perflowbackend.notification.command.domain.repository;

import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    List<FcmToken> findByEmployeeEmpId(String empId);

    Optional<FcmToken> findByFcmToken(String fcmToken);

    void deleteByFcmToken(String fcmToken);
}
