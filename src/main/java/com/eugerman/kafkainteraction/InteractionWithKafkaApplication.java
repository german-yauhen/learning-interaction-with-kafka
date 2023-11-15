package com.eugerman.kafkainteraction;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
public class InteractionWithKafkaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionWithKafkaApplication.class);
    private static final String DEMO_TOPIC = "demo-topic";

    public static void main(String[] args) {
        SpringApplication.run(InteractionWithKafkaApplication.class);

        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        Properties producerProperties = kafkaConfiguration.getProducerProperties();

        try (Producer<String, String> kafkaProducer = new KafkaProducer<>(producerProperties)) {
            kafkaProducer.send(
                    new ProducerRecord<>(DEMO_TOPIC, "alert", "demo alert"),
                    (metadata, exception) -> {
                        if (exception != null) {
                            LOGGER.error(ExceptionUtils.getRootCauseMessage(exception), exception);
                        } else {
                            LOGGER.debug("Message successfully sent -> topic '{}', partition '{}', offcet '{}'",
                                    metadata.topic(), metadata.partition(), metadata.offset());
                        }
                    }
            );
        } catch (KafkaException exc) {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
        }

        Properties consumerProperties = kafkaConfiguration.getConsumerProperties();
        try (Consumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerProperties)) {
            kafkaConsumer.subscribe(Collections.singleton(DEMO_TOPIC));
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(Integer.MAX_VALUE));
            records.forEach(record -> LOGGER.debug("Message consumed: {}:{}", record.key(), record.value()));
        } catch (KafkaException exc) {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
        }
    }

}
