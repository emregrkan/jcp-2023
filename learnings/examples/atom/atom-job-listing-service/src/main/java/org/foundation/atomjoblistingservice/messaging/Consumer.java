package org.foundation.atomjoblistingservice.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {
    @KafkaListener(topics = "job-listings.changed", groupId = "job-listings.consumer.g-1")
    public void listen(String message) {
        log.info("Message received: {}", message);
    }
}
