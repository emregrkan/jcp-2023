package org.foundation.atomjoblistingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories
@EnableMongoAuditing
public class AtomJobListingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtomJobListingServiceApplication.class, args);
	}
}
