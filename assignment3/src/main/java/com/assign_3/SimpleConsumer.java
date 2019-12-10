package com.assign_3;

import java.util.Properties;
import java.time.Duration;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class SimpleConsumer {
    /*
     * Thread for producer Talk to prof about parsing info from DB and to Send to
     * other kafka streams
     */
    public static void main(String[] args) throws Exception {
        // Kafka consumer configuration settings
        String topicName = "test";
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            // The buffer.memory controls the total amount of memory available to the
                @Override
            // producer for buffering.
                public void run() {
                    consumer.close();
                }
    
            });

        // Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));

        // print the topic name
        System.out.println("Subscribed to topic " + topicName);
        Duration time = Duration.ofSeconds(100);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(time);
            for (ConsumerRecord<String, String> record : records)
                // print the offset,key and value for the consumer records.
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());

        }
    }

}

