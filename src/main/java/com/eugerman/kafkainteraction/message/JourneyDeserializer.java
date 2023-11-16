package com.eugerman.kafkainteraction.message;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class JourneyDeserializer implements Deserializer<Journey> {

    @Override
    public Journey deserialize(String topic, byte[] data) {
        return SerializationUtils.deserialize(data);
    }
}
