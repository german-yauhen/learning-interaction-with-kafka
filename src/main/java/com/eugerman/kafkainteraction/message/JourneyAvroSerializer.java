package com.eugerman.kafkainteraction.message;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JourneyAvroSerializer implements Serializer<Journey> {

    @Override
    public byte[] serialize(String topic, Journey data) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(output, null);
            DatumWriter<Journey> writer = new ReflectDatumWriter<>(JourneySchema.SCHEMA);
            writer.write(data, binaryEncoder);
            binaryEncoder.flush();
            return output.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
