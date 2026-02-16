package com.example.projectapi.infra.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer; // ESSA É A CLASSE MÃE
import java.io.IOException;

public class CleanupDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return (value != null) ? value.toUpperCase().replaceAll("[^0-9X]", "") : null;
    }
}