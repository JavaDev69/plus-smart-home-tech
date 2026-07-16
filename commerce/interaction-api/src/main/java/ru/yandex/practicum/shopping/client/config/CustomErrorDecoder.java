package ru.yandex.practicum.shopping.client.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Andrew Vilkov
 * @created 16.07.2026 - 16:46
 * @project plus-smart-home-tech
 */
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("Decoding error response for methodKey: {}, response: {}", methodKey, response);
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
