package com.eugerman.kafkainteraction.message;

import java.util.Objects;

public final class Journey {

    private int id;
    private String origin;
    private String destination;
    private long departureDateTimeMs;
    private String description;

    public Journey() {
    }

    public Journey(int id, String origin, String destination, long departureDateTimeMs, String description) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTimeMs = departureDateTimeMs;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getDepartureDateTimeMs() {
        return departureDateTimeMs;
    }

    public void setDepartureDateTimeMs(long departureDateTimeMs) {
        this.departureDateTimeMs = departureDateTimeMs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Journey) obj;
        return this.id == that.id &&
                Objects.equals(this.origin, that.origin) &&
                Objects.equals(this.destination, that.destination) &&
                this.departureDateTimeMs == that.departureDateTimeMs &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, departureDateTimeMs, description);
    }

    @Override
    public String toString() {
        return "Journey[" +
                "id=" + id + ", " +
                "origin=" + origin + ", " +
                "destination=" + destination + ", " +
                "departureDateTimeMs=" + departureDateTimeMs + ", " +
                "description=" + description + ']';
    }

}
