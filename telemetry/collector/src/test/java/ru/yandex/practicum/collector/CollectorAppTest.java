package ru.yandex.practicum.collector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.collector.kafka.KafkaAvroProducer;

@SpringBootTest
class CollectorAppTest {
    @MockBean
    private KafkaAvroProducer kafkaAvroProducer;

    @Test
    void contextLoads() {
    }
}
