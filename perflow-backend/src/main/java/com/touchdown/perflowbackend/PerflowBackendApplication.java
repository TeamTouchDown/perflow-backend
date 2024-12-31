package com.touchdown.perflowbackend;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableRabbit
@SpringBootApplication
public class PerflowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerflowBackendApplication.class, args);
    }

}
