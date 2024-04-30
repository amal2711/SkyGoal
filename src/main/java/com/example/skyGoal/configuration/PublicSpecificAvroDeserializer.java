package com.example.skyGoal.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.IOException;
import java.util.Map;

public class PublicSpecificAvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {
    private final Class<T> targetType;

    public PublicSpecificAvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration can be set here if needed
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        DatumReader<T> datumReader = new SpecificDatumReader<>(targetType);
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        try {
            return datumReader.read(null, decoder);
        } catch (IOException e) {
            throw new RuntimeException("Deserialization error:", e);
        }
    }

    @Override
    public void close() {
        // Close resources here if necessary
    }

    // Getter for targetType
    public Class<T> getTargetType() {
        return targetType;
    }
}
