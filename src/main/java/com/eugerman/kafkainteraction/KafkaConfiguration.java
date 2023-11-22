package com.eugerman.kafkainteraction;

import java.util.Properties;

public class KafkaConfiguration {

    private final Properties producerProperties;
    private final Properties consumerProperties;

    public KafkaConfiguration() {
        this.producerProperties = new Properties();
        this.producerProperties.setProperty("bootstrap.servers", "localhost:9094");
        this.producerProperties.setProperty("client.id", "com.eugerman.interaction.with.kafka");
        this.producerProperties.setProperty("schema.registry.url", "http://localhost:8081");
        this.producerProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producerProperties.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");

        this.consumerProperties = new Properties();
        this.consumerProperties.setProperty("bootstrap.servers", "localhost:9094");
        this.producerProperties.setProperty("client.id", "com.eugerman.interaction.with.kafka");
        this.consumerProperties.setProperty("group.id", "com.eugerman.interaction.with.kafka");
        this.consumerProperties.setProperty("schema.registry.url", "http://localhost:8081");
        this.consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumerProperties.setProperty("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
    }

    public Properties getProducerProperties() {
        return this.producerProperties;
    }

    public Properties getConsumerProperties() {
        return this.consumerProperties;
    }
}
