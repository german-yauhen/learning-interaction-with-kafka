package com.eugerman.kafkainteraction;

import com.eugerman.kafkainteraction.message.Journey;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@SpringBootApplication
public class InteractionWithKafkaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionWithKafkaApplication.class);
    private static final String JOURNEYS_TOPIC = "journeys";

    public static void main(String[] args) {
        SpringApplication.run(InteractionWithKafkaApplication.class);

        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        Properties producerProperties = kafkaConfiguration.getProducerProperties();

        Producer<String, Journey> producer = new KafkaProducer<>(producerProperties);
        try {
            Journey journey = new Journey(
                    1, "Poland", "Italy", Instant.now().toEpochMilli(), "A direct flight from Warsaw");

            producer.send(
                    new ProducerRecord<>(JOURNEYS_TOPIC, "journey", journey),
                    (metadata, exception) -> {
                        if (exception != null) {
                            LOGGER.error(ExceptionUtils.getRootCauseMessage(exception), exception);
                        } else {
                            LOGGER.debug("Message successfully sent -> topic '{}', partition '{}', offset '{}'",
                                    metadata.topic(), metadata.partition(), metadata.offset());
                        }
                    }
            );
        } catch (KafkaException exc) {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
        } finally {
            producer.flush();
            producer.close();
        }

        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

        Properties consumerProperties = kafkaConfiguration.getConsumerProperties();
        try (Consumer<String, Journey> consumer = new KafkaConsumer<>(consumerProperties)) {
            consumer.subscribe(Collections.singleton(JOURNEYS_TOPIC));
            ConsumerRecords<String, Journey> records = consumer.poll(Duration.ofMillis(Integer.MAX_VALUE));
            records.forEach(record -> {
                LOGGER.debug("Message consumed: {}:{}", record.key(), record.value());
                currentOffsets.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset())
                );
            });
            consumer.commitAsync(currentOffsets, (offsets, exc) -> {
                if (exc != null) {
                    LOGGER.error(ExceptionUtils.getRootCauseMessage(exc));
                } else {
                    LOGGER.debug("Committed {} offsets: {}",
                            offsets.size(),
                            offsets.values().stream()
                                    .map(OffsetAndMetadata::offset)
                                    .map(Objects::toString)
                                    .collect(Collectors.joining(","))
                    );
                }
            });
        } catch (Exception exc) {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
        }
    }

}
