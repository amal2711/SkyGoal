package com.example.skyGoal;

import com.example.skyGoal.converterHelper.AvroConverter;
import com.example.skyGoal.entity.FootballMatch;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class SerializationTest {

    @Test
    public void testFootballMatchSerialization() {
        // Hier den tatsächlichen Constructor Ihrer FootballMatch Klasse verwenden
        FootballMatch match = new FootballMatch("matchId", "Team A", "Team B", 1, 0, 90, LocalDateTime.now(), "Location");
        GenericRecord record = AvroConverter.convertToAvroRecord(match);

        byte[] data = serialize(record, FootballMatch.SCHEMA);
        GenericRecord result = deserialize(data, FootballMatch.SCHEMA);

        assertEquals(match.getMatchId(), result.get("matchId").toString());
    }

    private byte[] serialize(GenericRecord record, Schema schema) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
        try {
            writer.write(record, encoder);
            encoder.flush();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    private GenericRecord deserialize(byte[] data, Schema schema) {
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        GenericDatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
        try {
            return reader.read(null, decoder);
        } catch (IOException e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }
}

