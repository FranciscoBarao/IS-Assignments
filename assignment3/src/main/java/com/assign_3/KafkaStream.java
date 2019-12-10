package com.assign_3;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.Duration;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaStream {
    /*
     * Thread for producer Talk to prof about parsing info from DB and to Send to
     * other kafka streams
     */
    public static void main(String[] args) throws Exception {

        // Kafka consumer configuration settings
        String topic = "Sales";
        String topic2 = "Purchases";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        // Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topic));

        Duration time = Duration.ofSeconds(100);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(time);
            for (ConsumerRecord<String, String> record : records) {
                if (record.key().equalsIgnoreCase("Sale")) {
                    // Print value
                    System.out.printf("value = %s\n", record.value());
                    String parts[] = record.value().split(" ");
                    System.out.println("\nParts");
                    for (String s : parts) {
                        System.out.println(s);
                    }
                }
            }
        }
    }

}

class ResultsProducer extends Thread {

    public void run() {
        System.out.println("Producer thread running");

        // Assign topicName to string variable
        String topic = "Sales";

        // create instance for properties to access producer configs
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        // This might change..
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    }
}