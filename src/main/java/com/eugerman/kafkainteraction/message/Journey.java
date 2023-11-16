package com.eugerman.kafkainteraction.message;

import java.io.Serializable;
import java.time.LocalDateTime;

public record Journey(int id, String origin, String destination, LocalDateTime departureDateTime, String description) implements Serializable {
}
