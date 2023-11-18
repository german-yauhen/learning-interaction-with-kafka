package com.eugerman.kafkainteraction.message;

public record Journey(int id, String origin, String destination, long departureDateTimeMs, String description) {
}
