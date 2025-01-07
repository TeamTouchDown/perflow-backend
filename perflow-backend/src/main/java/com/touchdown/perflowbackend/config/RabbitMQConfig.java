package com.touchdown.perflowbackend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "notification.exchange";
    public static final String QUEUE_NAME = "notification.queue";
    public static final String ROUTING_KEY = "notification.key";

    public static final String DLX_NAME = "notification.dlx";
    public static final String DLQ_NAME = "notification.dlq";
    public static final String DLQ_ROUTING_KEY = "notification.dlq";

    @Bean
    public TopicExchange notificationExchange() {

        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue notificationQueue() {

        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding bindingNotificationQueue(
            @Qualifier("notificationQueue") Queue notificationQueue,
            @Qualifier("notificationExchange") TopicExchange notificationExchange
    ) {

        return BindingBuilder.bind(notificationQueue)
                .to(notificationExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public TopicExchange notificationDLX() {

        return new TopicExchange(DLX_NAME);
    }

    @Bean
    public Queue notificationDLQ() {

        return QueueBuilder.durable(DLQ_NAME)
                // DLQ에 쌓인 메시지를 일정 시간 후 재시도
                .withArgument("x-message-ttl", 10000) // 1분 (원하는 시간으로)
                // TTL이 끝나면 원본 Exchange/Key로 반환
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding bindingNotificationDLQ(
            @Qualifier("notificationDLQ") Queue notificationDLQ,
            @Qualifier("notificationDLX") TopicExchange notificationDLX
    ) {

        return BindingBuilder.bind(notificationDLQ)
                .to(notificationDLX)
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}
