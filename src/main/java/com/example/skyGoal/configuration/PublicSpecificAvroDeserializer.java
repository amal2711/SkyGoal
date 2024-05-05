//package com.example.skyGoal.configuration;
//
//import lombok.Getter;
//import org.apache.avro.specific.SpecificRecordBase;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.apache.avro.io.DatumReader;
//import org.apache.avro.io.Decoder;
//import org.apache.avro.io.DecoderFactory;
//import org.apache.avro.specific.SpecificDatumReader;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Getter
//public class PublicSpecificAvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {
//
//    private final Class<T> targetType;
//    private String schemaRegistryUrl;
//
//    public PublicSpecificAvroDeserializer(Class<T> targetType, String schemaRegistryUrl) {
//        this.targetType = targetType;
//        this.schemaRegistryUrl = schemaRegistryUrl;
//    }
//
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//        // Hier könnten weitere Konfigurationen hinzugefügt werden
//    }
//
//    @Override
//    public T deserialize(String topic, byte[] data) {
//        if (data == null) return null;
//        DatumReader<T> datumReader = new SpecificDatumReader<>(targetType);
//        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
//        try {
//            return datumReader.read(null, decoder);
//        } catch (IOException e) {
//            throw new RuntimeException("Deserialization error: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void close() {
//        // Schließe Ressourcen, falls nötig
//    }
//}
//
