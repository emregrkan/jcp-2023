package com.obss.metropositionsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MetroPositionsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MetroPositionsServiceApplication.class, args);
    }
}
