package com.eugerman.kafkainteraction;

import java.util.Properties;

public class KafkaConfiguration {

    private final Properties producerProperties;
    private final Properties consumerProperties;

    public KafkaConfiguration() {
        this.producerProperties = new Properties();
        this.producerProperties.setProperty("bootstrap.servers", "0.0.0.0:9094");
        this.producerProperties.setProperty("client.id", "com.eugerman.interaction.with.kafka");
        this.producerProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.producerProperties.setProperty("value.serializer", "com.eugerman.kafkainteraction.message.JourneySerializer");
        this.producerProperties.setProperty("value.deserializer", "com.eugerman.kafkainteraction.message.JourneyDeserializer");

        this.consumerProperties = new Properties();
        this.consumerProperties.setProperty("bootstrap.servers", "0.0.0.0:9094");
        this.producerProperties.setProperty("client.id", "com.eugerman.interaction.with.kafka");
        this.consumerProperties.setProperty("group.id", "com.eugerman.interaction.with.kafka");
        this.consumerProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumerProperties.setProperty("value.serializer", "com.eugerman.kafkainteraction.message.JourneySerializer");
        this.consumerProperties.setProperty("value.deserializer", "com.eugerman.kafkainteraction.message.JourneyDeserializer");
    }

    public Properties getProducerProperties() {
        return this.producerProperties;
    }

    public Properties getConsumerProperties() {
        return this.consumerProperties;
    }
}
