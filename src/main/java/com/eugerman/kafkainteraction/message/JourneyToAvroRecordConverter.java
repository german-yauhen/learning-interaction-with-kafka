package com.eugerman.kafkainteraction.message;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;

public class JourneyToAvroRecordConverter {

    public static GenericRecord convert(Journey journey) {
        return new GenericRecordBuilder(JourneySchema.SCHEMA)
                .set("id", journey.id())
                .set("origin", journey.origin())
                .set("destination", journey.destination())
                .set("departureDateTimeMs", journey.departureDateTimeMs())
                .set("description", journey.description())
                .build();

    }

}
