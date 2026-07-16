package ru.yandex.practicum.shopping.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.shopping.client.WarehouseClient;

/**
 * @author Andrew Vilkov
 * @created 10.07.2026 - 17:27
 * @project plus-smart-home-tech
 */
@EnableFeignClients(clients = WarehouseClient.class)
@EnableDiscoveryClient
@SpringBootApplication
public class ShoppingCartApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApp.class, args);
    }
}
