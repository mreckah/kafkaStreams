package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TextCleanerApp {
    public static void main(String[] args) {

        // Configuration Kafka Streams
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "text-cleaner-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        // Lecture du topic text-input
        KStream<String, String> inputStream = builder.stream("text-input");

        // Nettoyage des messages
        KStream<String, String> cleanedStream = inputStream.mapValues(value -> {
            if (value == null) return "";
            return value.trim()
                        .replaceAll("\\s+", " ")
                        .toUpperCase();
        });

        // Mots interdits
        List<String> forbiddenWords = Arrays.asList("HACK", "SPAM", "XXX");

        // Filtrage valides / invalides
        KStream<String, String>[] branches = cleanedStream.branch(
                (key, value) -> !value.isBlank() &&
                                value.length() <= 100 &&
                                forbiddenWords.stream().noneMatch(value::contains),
                (key, value) -> true
        );

        // Routage
        branches[0].to("text-clean", Produced.with(Serdes.String(), Serdes.String()));
        branches[1].to("text-dead-letter", Produced.with(Serdes.String(), Serdes.String()));

        // Lancement de l'application
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
