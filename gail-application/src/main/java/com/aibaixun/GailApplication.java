package com.aibaixun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class GailApplication {
    public static void main(String[] args) {
        SpringApplication.run(GailApplication.class, args);
    }
}
