package com.eugerman.kafkainteraction.consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class BasicConsumeWithShutdownOnWakeupLoop<K, V> implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicConsumeWithShutdownOnWakeupLoop.class);
    private final List<String> topics;
    private final Consumer<K, V> consumer;
    private final CountDownLatch shutdownLatch;

    public BasicConsumeWithShutdownOnWakeupLoop(Consumer<K, V> consumer, List<String> topics) {
        this.topics = topics;
        this.consumer = consumer;
        this.shutdownLatch = new CountDownLatch(1);
    }

    public abstract void process(ConsumerRecord<K, V> record);

    /**
     *  “at least once” delivery since the commit follows the message processing
     */
    @Override
    public void run() {
        try {
            consumer.subscribe(topics);
            while (isActive()) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(Integer.MAX_VALUE));
                records.forEach(this::process);
                doCommitSync();
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

    public void shutdown() throws InterruptedException {
        consumer.wakeup();
        shutdownLatch.await();
    }

    private boolean isActive() {
        return true;
    }

    private void doCommitSync() {
        try {
            consumer.commitSync();
        } catch (WakeupException exc) {
            // The loop is being interrupted,
            // but finish the commit first and then rethrow the exception so that the main loop can exit
            // !!! The recursive call is safe since the wakeup will only be triggered once
            consumer.commitSync();
            throw exc;
        } catch (CommitFailedException exc) {
            // The exc is thrown when the commit cannot be completed because the group has been rebalanced
            LOGGER.error(ExceptionUtils.getRootCauseMessage(exc), exc);
            // If there is any state that depends on the commit we can clean it up
            // or rollback changes otherwise the exception mey be skipped
        }
    }
}
