package com.eugerman.kafkainteraction.message;

import org.apache.avro.Schema;

public class JourneySchema {

    public static final Schema SCHEMA = new Schema.Parser().parse("""
             {
            	"namespace":"com.eugerman.kafkainteraction",
            	"type":"record",
            	"name":"Journey",
            	"fields":[
            		{"name":"id", "type":"int"},
            		{"name":"origin", "type":"string"},
            		{"name":"destination", "type":"string"},
            		{"name":"departureDateTimeMs", "type":"long"},
            		{"name":"description", "type":["null", "string"]}
            	]
            }""");

}
