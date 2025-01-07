package com.touchdown.perflowbackend.employee.command.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    private final String SECRET_KEY; // 16-byte key for AES-128

    public EncryptionUtil(
            @Value("${AES_SECRET}") String secret
    ) {
        SECRET_KEY = secret;
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    }

    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        byte[] iv = new byte[IV_LENGTH_BYTE]; // In a real application, use a securely generated IV
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        byte[] iv = new byte[IV_LENGTH_BYTE]; // IV should be the same as used during encryption
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}
