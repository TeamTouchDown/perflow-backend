package com.touchdown.perflowbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class FCMConfig {

    private final Environment env;

    public FCMConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // JSON 데이터를 application.yml에서 읽기
        String jsonConfig = buildJsonFromYaml();

        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(jsonConfig.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

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

    private String buildJsonFromYaml() {
        return String.format("{\"type\":\"%s\",\"project_id\":\"%s\",\"private_key_id\":\"%s\",\"private_key\":\"%s\",\"client_email\":\"%s\",\"client_id\":\"%s\",\"auth_uri\":\"%s\",\"token_uri\":\"%s\",\"auth_provider_x509_cert_url\":\"%s\",\"client_x509_cert_url\":\"%s\",\"universe_domain\":\"%s\"}",
                env.getProperty("fcm.service-account.type"),
                env.getProperty("fcm.service-account.project_id"),
                env.getProperty("fcm.service-account.private_key_id"),
                env.getProperty("fcm.service-account.private_key").replace("\n", "\\n"),
                env.getProperty("fcm.service-account.client_email"),
                env.getProperty("fcm.service-account.client_id"),
                env.getProperty("fcm.service-account.auth_uri"),
                env.getProperty("fcm.service-account.token_uri"),
                env.getProperty("fcm.service-account.auth_provider_x509_cert_url"),
                env.getProperty("fcm.service-account.client_x509_cert_url"),
                env.getProperty("fcm.service-account.universe_domain")
        );
    }
}
