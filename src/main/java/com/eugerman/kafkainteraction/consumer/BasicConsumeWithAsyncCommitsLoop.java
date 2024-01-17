package com.eugerman.kafkainteraction.consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public abstract class BasicConsumeWithAsyncCommitsLoop<K, V> implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(BasicConsumeWithAsyncCommitsLoop.class);

    private final List<String> topics;
    private final Consumer<K, V> consumer;
    private final CountDownLatch shutdownLatch;

    public BasicConsumeWithAsyncCommitsLoop(Consumer<K, V> consumer, List<String> topics) {
        this.topics = topics;
        this.consumer = consumer;
        this.shutdownLatch = new CountDownLatch(1);
    }

    public abstract void process(ConsumerRecord<K, V> record);

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);
            while (isActive()) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(Integer.MAX_VALUE));
                records.forEach(this::process);
                consumer.commitAsync(offsetCommitCallback());
            }
        } catch (WakeupException exc) {
            LOGGER.warn("Expected WakeupException exception during closing the consumer");
        } catch (Exception exc) {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
            // As an option - a rollback
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    private OffsetCommitCallback offsetCommitCallback() {
        return (offsets, exc) -> {
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
        };
    }

    public void shutdown() throws InterruptedException {
        consumer.wakeup();
        shutdownLatch.await();
    }

    private boolean isActive() {
        return true;
    }
}
