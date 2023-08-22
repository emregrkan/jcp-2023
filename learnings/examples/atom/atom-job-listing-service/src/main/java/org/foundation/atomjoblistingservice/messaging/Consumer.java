package org.foundation.atomjoblistingservice.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {
    @KafkaListener(topics = "job-listings.changed", groupId = "consumer.job-listing-service")
    public void listenJobListingEvents(String message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "profiles.created", groupId = "consumer.job-listing-service")
    public void listenProfileEvents(String message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "applications.created", groupId = "consumer.job-listing-service")
    public void listenApplicationEvents(String message) {
        log.info("Message received: {}", message);
    }
}
