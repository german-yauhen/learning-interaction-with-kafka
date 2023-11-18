package com.eugerman.kafkainteraction;

import com.eugerman.kafkainteraction.message.Journey;
import com.eugerman.kafkainteraction.message.JourneyToAvroRecordConverter;
import org.apache.avro.generic.GenericRecord;
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
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
public class InteractionWithKafkaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionWithKafkaApplication.class);
    private static final String JOURNEYS_TOPIC = "journeys";

    public static void main(String[] args) {
        SpringApplication.run(InteractionWithKafkaApplication.class);

        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();
        Properties producerProperties = kafkaConfiguration.getProducerProperties();

        Producer<String, GenericRecord> kafkaProducer = new KafkaProducer<>(producerProperties);
        try {
            Journey journey = new Journey(
                    1, "Poland", "Italy", Instant.now().toEpochMilli(), "A direct flight from Warsaw");

            GenericRecord record = JourneyToAvroRecordConverter.convert(journey);

            kafkaProducer.send(
                    new ProducerRecord<>(JOURNEYS_TOPIC, "journey", record),
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
            kafkaProducer.flush();
            kafkaProducer.close();
        }

        Properties consumerProperties = kafkaConfiguration.getConsumerProperties();
        try (Consumer<String, GenericRecord> kafkaConsumer = new KafkaConsumer<>(consumerProperties)) {
            kafkaConsumer.subscribe(Collections.singleton(JOURNEYS_TOPIC));
            ConsumerRecords<String, GenericRecord> records = kafkaConsumer.poll(Duration.ofMillis(Integer.MAX_VALUE));
            records.forEach(record -> LOGGER.debug("Message consumed: {}:{}", record.key(), record.value()));
        } catch (KafkaException exc) {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
        }
    }

}
