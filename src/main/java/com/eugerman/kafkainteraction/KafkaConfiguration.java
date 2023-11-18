package com.eugerman.kafkainteraction;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

public class KafkaConfiguration {

    private final Properties producerProperties;
    private final Properties consumerProperties;

    public KafkaConfiguration() {
        this.producerProperties = new Properties();
        this.producerProperties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9094");
        this.producerProperties.setProperty(CLIENT_ID_CONFIG, "com.eugerman.interaction.with.kafka");
        this.producerProperties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.producerProperties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer");
        this.producerProperties.setProperty("schema.registry.url", "http://localhost:8081");

        this.consumerProperties = new Properties();
        this.consumerProperties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9094");
        this.producerProperties.setProperty(CLIENT_ID_CONFIG, "com.eugerman.interaction.with.kafka");
        this.consumerProperties.setProperty(GROUP_ID_CONFIG, "com.eugerman.interaction.with.kafka");
        this.consumerProperties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumerProperties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        this.consumerProperties.setProperty("schema.registry.url", "http://localhost:8081");
    }

    public Properties getProducerProperties() {
        return this.producerProperties;
    }

    public Properties getConsumerProperties() {
        return this.consumerProperties;
    }
}
