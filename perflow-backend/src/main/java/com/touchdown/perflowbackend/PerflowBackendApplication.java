package com.touchdown.perflowbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PerflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerflowBackendApplication.class, args);
    }

}
