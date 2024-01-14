package com.eugerman.kafkainteraction.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BasicConsumeLoop<K, V> implements Runnable {
    private final List<String> topics;
    private final Consumer<K, V> consumer;
    private final Duration consumeTimeout;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;


    public BasicConsumeLoop(Properties properties, List<String> topics, Duration consumeTimeout) {
        this.topics = topics;
        this.consumer = new KafkaConsumer<>(properties);
        this.consumeTimeout = consumeTimeout;
        this.shutdown = new AtomicBoolean(false);
        this.shutdownLatch = new CountDownLatch(1);
    }

    public abstract void process(ConsumerRecord<K, V> record);

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);
            while (isActive()) {
                ConsumerRecords<K, V> records = consumer.poll(consumeTimeout);
                records.forEach(this::process);
            }
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    private boolean isActive() {
        return !shutdown.get();
    }

    public void shutdown() throws InterruptedException {
        this.shutdown.set(true);
        this.shutdownLatch.await();
    }
}