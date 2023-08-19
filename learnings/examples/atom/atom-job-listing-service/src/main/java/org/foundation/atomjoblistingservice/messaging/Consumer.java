package org.foundation.atomjoblistingservice.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {
    @KafkaListener(topics = "job-listings.changed", groupId = "job-listings.consumer.g-1")
    public void listenJobListingsEvents(String message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "profiles.changed", groupId = "profiles.consumer.g-1")
    public void listenProfilesEvents(String message) {
        log.info("Message received: {}", message);
    }
}
