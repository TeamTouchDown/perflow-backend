package com.touchdown.perflowbackend.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class QRCodeGenerator {

    private final RedisTemplate<String, String> redisTemplate;

    // QR 코드 생성 및 Redis 저장
    public String generateQRCode(String empId) {
        // 6자리 랜덤 숫자 생성
        String qrCode = String.format("%06d", new Random().nextInt(1_000_000));
        // Redis에 저장 (1분 유효 시간)
        redisTemplate.opsForValue().set("qr:" + empId, qrCode, 1, TimeUnit.MINUTES);
        log.info("Generated QR Code: {}", qrCode);
        // QR 코드를 이미지(Base64)로 변환하여 반환
        return generateQRCodeImageBase64(qrCode);
    }

    // Redis에서 QR 코드 검증
    public boolean validateQRCode(String empId, String qrCode) {

        String storedQRCode = redisTemplate.opsForValue().get("qr:" + empId);
        // Redis에 저장된 값이 없거나 코드가 일치하지 않으면 예외 발생
        if (storedQRCode == null || !storedQRCode.equals(qrCode)) {
            throw new IllegalArgumentException("올바르지 않은 QR Code");
        }

        // 검증 성공 시 Redis에서 삭제
        redisTemplate.delete("qr:" + empId);
        return true;
    }

    // QR 코드 이미지를 Base64로 생성
    private String generateQRCodeImageBase64(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream, new MatrixToImageConfig());
            byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();
            return java.util.Base64.getEncoder().encodeToString(qrCodeBytes);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("QR 생성 실패", e);
        }
    }
}
