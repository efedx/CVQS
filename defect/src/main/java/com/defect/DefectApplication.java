package com.defect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DefectApplication {
    public static void main(String[] args) {
        SpringApplication.run(DefectApplication.class, args);
    }
}