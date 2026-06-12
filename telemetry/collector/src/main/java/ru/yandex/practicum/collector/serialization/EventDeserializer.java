package ru.yandex.practicum.collector.serialization;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.kafka.EventTopics;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class EventDeserializer implements Deserializer<SpecificRecordBase> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();

    @Override
    public SpecificRecordBase deserialize(String topic, byte[] bytes) {
        try {
            if (bytes != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(bytes, null);
                DatumReader<SpecificRecordBase> reader =
                        switch (topic) {
                            case EventTopics.SENSOR_TOPIC_V1 ->
                                    new SpecificDatumReader<>(SensorEventAvro.getClassSchema());
                            case EventTopics.HUB_TOPIC_V1 -> new SpecificDatumReader<>(HubEventAvro.getClassSchema());
                            default -> throw new IllegalStateException("Unknown topic: " + topic);
                        };
                return reader.read(null, decoder);
            }
        } catch (Exception ex) {
            throw new SerializationException("Ошибка десереализации данных из топика [" + topic + "]", ex);
        }
        return null;
    }

}
