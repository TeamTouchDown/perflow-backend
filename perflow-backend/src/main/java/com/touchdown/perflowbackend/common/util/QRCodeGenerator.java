package com.touchdown.perflowbackend.common.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class QRCodeGenerator {

    private final RedisTemplate<String, String> redisTemplate;

    public QRCodeGenerator(RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    // QR 코드 생성 및 Redis 저장
    public String generateQRCode(String empId) {

        // 6자리 랜덤 숫자 생성
        String qrCode = String.format("%06d", new Random().nextInt(1000000));

        // Redis에 저장 (1분 유효 시간)
        redisTemplate.opsForValue().set("qr:" + empId, qrCode, 1, TimeUnit.MINUTES);

        // Base64 포맷으로 변환 (프론트엔드 전달용)
        return qrCode;
    }

    // Redis에서 QR 코드 검증
    public boolean validateQRCode(String empId, String qrCode) {

        String storedQRCode = redisTemplate.opsForValue().get("qr:" + empId);

        // QR 코드 일치 여부 확인
        return qrCode.equals(storedQRCode);
    }
}
