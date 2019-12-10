package com.assign_3;

import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProducer {
    /*
     * Thread for producer Talk to prof about parsing info from DB and to Send to
     * other kafka streams
     */
    public static void main(final String[] args) throws Exception {
        System.out.println("Producer thread running");

        // Assign topicName to string variable
        final String topic = "test";

        // create instance for properties to access producer configs
        final Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        // This might change..
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.LongSerializer");

        final Random random = new Random();
        // Get random Item
        // String i = items.get(random.nextInt(items.size()));
        // Get random Country
        // String c = countries.get(random.nextInt(countries.size()));
        // Get random Price
        final int max = 20, min = 1;
        // int p = random.nextInt(max - min + 1) + min;
        // Get random Units
        final int u = random.nextInt(max - min + 1) + min;

        final Producer<String, Long> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<String, Long>(topic, "-->", (long) u));

        System.out.println("Message sent successfully to topic " + topic);
        producer.close();
    }
}