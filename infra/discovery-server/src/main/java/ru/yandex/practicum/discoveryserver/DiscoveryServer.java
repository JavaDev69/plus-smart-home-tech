package ru.yandex.practicum.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Andrew Vilkov
 * @created 09.07.2026 - 10:07
 * @project plus-smart-home-tech
 */
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryServer {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServer.class, args);
    }
}
