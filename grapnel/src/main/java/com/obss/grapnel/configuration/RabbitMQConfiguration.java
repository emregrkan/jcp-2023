package com.obss.grapnel.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue queueUserProfile() {
        return new Queue("q.user-profile", false);
    }
}
