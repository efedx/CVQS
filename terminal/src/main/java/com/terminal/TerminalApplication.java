package com.terminal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.terminal", "com.amqp"}
        )
@EnableDiscoveryClient
@EntityScan({"com.common", "com.terminal"})
public class TerminalApplication {

    public static void main(String[] args) {

        SpringApplication.run(TerminalApplication.class);
    }

}
