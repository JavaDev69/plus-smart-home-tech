package ru.yandex.practicum.shopping.client.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andrew Vilkov
 * @created 16.07.2026 - 16:49
 * @project plus-smart-home-tech
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
