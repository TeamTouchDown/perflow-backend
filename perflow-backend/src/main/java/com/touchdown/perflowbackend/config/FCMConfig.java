package com.touchdown.perflowbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;

@Configuration
@RequiredArgsConstructor
public class FCMConfig {

    private final Environment env;

    @Bean
    public FirebaseApp firebaseApp() throws Exception {

        String privateKey = env.getProperty("fcm.service-account.private_key");
        if (privateKey == null) {
            throw new IllegalStateException("FCM private key is missing in configuration");
        }

        privateKey = privateKey.replace("\\n", "\n");

        String jsonConfig = String.format(
                "{\"type\":\"%s\",\"project_id\":\"%s\",\"private_key_id\":\"%s\",\"private_key\":\"%s\",\"client_email\":\"%s\",\"client_id\":\"%s\",\"auth_uri\":\"%s\",\"token_uri\":\"%s\",\"auth_provider_x509_cert_url\":\"%s\",\"client_x509_cert_url\":\"%s\"}",
                env.getProperty("fcm.service-account.type"),
                env.getProperty("fcm.service-account.project_id"),
                env.getProperty("fcm.service-account.private_key_id"),
                privateKey,
                env.getProperty("fcm.service-account.client_email"),
                env.getProperty("fcm.service-account.client_id"),
                env.getProperty("fcm.service-account.auth_uri"),
                env.getProperty("fcm.service-account.token_uri"),
                env.getProperty("fcm.service-account.auth_provider_x509_cert_url"),
                env.getProperty("fcm.service-account.client_x509_cert_url")
        );

        // FirebaseOptions 설정
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(jsonConfig.getBytes())))
                .build();

        // FirebaseApp 초기화
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
