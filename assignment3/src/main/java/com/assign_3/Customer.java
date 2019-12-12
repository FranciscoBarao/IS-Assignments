package com.assign_3;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class Customer {
    /*
     * Thread for producer Talk to prof about parsing info from DB and to Send to
     * other kafka streams
     * 
     * 
     * 
     * transform -> splits shit mapValues(v->transform(v))
     * groupByKey((GRouped.with(Serdes.String(),Serdes.Double()).reduce((v1,v2) ->
     * v1+v2)
     */
    public static void main(String[] args) throws Exception {
        // Kafka consumer configuration settings
        String topicName = "DBInfo";
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "customer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                consumer.close();
            }
        });

        // Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(topicName));
        // print the topic name
        System.out.println("Subscribed to topic " + topicName);
        Duration time = Duration.ofSeconds(100);

        DBInfoProducer t1 = new DBInfoProducer();
        t1.start();

        CopyOnWriteArrayList<String> c = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<String> i = new CopyOnWriteArrayList<>();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(time);
            i.clear();
            c.clear();
            for (ConsumerRecord<String, String> record : records) {
                // String to Json to Hashmap
                HashMap<String, Object> result = new ObjectMapper().readValue(record.value(), HashMap.class);
                // Convert Payload to Hashmap
                HashMap<String, Object> object = (HashMap<String, Object>) result.get("payload");

                System.out.println("\nHERE -> " + result.get("payload"));
                // System.out.println("\nGIMME -> " + country.get("name"));

                System.out.println("\nName: " + object.get("name") + " | Id: " + object.get("id") + " | Type: "
                        + object.get("data_type"));

                String s = object.get("data_type").toString();

                if (s.equals("country")) {
                    c.add(object.get("name").toString());
                } else
                    i.add(object.get("id").toString());

            }
            t1.setArrays(i, c);
        }
    }
}

class DBInfoProducer extends Thread {
    private CopyOnWriteArrayList<String> countries;
    private CopyOnWriteArrayList<String> items;

    DBInfoProducer() {
        this.countries = new CopyOnWriteArrayList<>();
        this.items = new CopyOnWriteArrayList<>();
    }

    public void setArrays(CopyOnWriteArrayList<String> items, CopyOnWriteArrayList<String> countries) {
        this.items = items;
        this.countries = countries;
    }

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

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (countries.size() > 0 && items.size() > 0)
                populateSales(props, topic);
        }
    }

    void populateSales(Properties props, String topic) {
        System.out.println("\nPopulateSales");
        Random random = new Random();
        // Get random Item
        String i = items.get(random.nextInt(items.size()));
        // Get random Country
        System.out.println("\nCountries size: " + countries.size());
        String c = countries.get(random.nextInt(countries.size()));
        System.out.println(c);
        // Get random Price
        int max = 20, min = 1;
        int p = random.nextInt(max - min + 1) + min;
        // Get random Units
        int u = random.nextInt(max - min + 1) + min;

        Producer<String, String> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<String, String>(topic, i, p + " " + u + " " + c));

        System.out.println("Message sent successfully to topic " + topic);
        producer.close();
    }
}