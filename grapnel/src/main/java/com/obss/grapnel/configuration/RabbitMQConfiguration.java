package com.obss.grapnel.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue queueUserUrl() {
        return new Queue("q.candidate-url", false);
    }

    @Bean
    public Queue queueUserProfile() {
        return new Queue("q.candidate-profile", false);
    }
}
