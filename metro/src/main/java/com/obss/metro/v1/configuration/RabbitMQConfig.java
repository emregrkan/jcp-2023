package com.obss.metro.v1.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queueUserUrl() {
        return new Queue("q.user-url", false);
    }
}
