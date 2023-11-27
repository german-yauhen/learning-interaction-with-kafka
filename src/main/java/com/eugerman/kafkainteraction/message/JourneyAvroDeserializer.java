package com.eugerman.kafkainteraction.message;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class JourneyAvroDeserializer implements Deserializer<Journey> {

    @Override
    public Journey deserialize(String topic, byte[] data) {
        BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(data, null);
        DatumReader<Journey> reader = new ReflectDatumReader<>(Journey.class);
        reader.setSchema(JourneySchema.SCHEMA);
        try {
            return reader.read(null, binaryDecoder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
