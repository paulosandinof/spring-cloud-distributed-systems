package com.sandino.peopleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PeopleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeopleServiceApplication.class, args);
    }
}
