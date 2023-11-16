package com.eugerman.kafkainteraction.message;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

public class JourneySerializer implements Serializer<Journey> {

    @Override
    public byte[] serialize(String topic, Journey data) {
        return data != null ? SerializationUtils.serialize(data) : null;
    }

}
