package com.touchdown.perflowbackend;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableRabbit
@EnableAsync
@SpringBootApplication
public class PerflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerflowBackendApplication.class, args);
    }

}
