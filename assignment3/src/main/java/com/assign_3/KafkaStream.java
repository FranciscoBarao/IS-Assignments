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
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KGroupedTable;
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
        String topicSales = "Sales";
        String topicPurchases = "Purchases";
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        StreamsBuilder builder = new StreamsBuilder();

        // id , 10 10 Spain
        KStream<String, String> salesStream = builder.stream(topicSales);
        KStream<String, String> purchasesStream = builder.stream(topicPurchases);

        KTable<String, Double> revenueTable = salesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        KTable<String, Double> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            return (valueRevennue - valueExpenses);
        });

        KGroupedStream<String, Double> s = revenueTable.toStream().groupBy((v1, v2) -> null);
        KTable<String, Double> totalRevenue = s.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        System.out.println("\n Total revenue: " + totalRevenue);

        // Total profit needs a groupBy(k,v -> null)
        /*
         * // second step: compute average for each 2-tuple final KTable<String,Double>
         * average = countAndSum.mapValues( new ValueMapper<Tuple2<Long, Long>,
         * Double>() {
         * 
         * @Override public Double apply(Tuple2<Long, Long> value) { return value.value2
         * / (double) value.value1; } });
         */

        // Calcular medias -> aggregate - fazer soma e contar quantos ja ocorreram ...
        // ("total, counter")
        // agreggate passa id, value,"total,count"

    }

    private static Double transform(String s) {
        String parts[] = s.split(" ");
        Double i = Double.parseDouble(parts[0]);
        Double j = Double.parseDouble(parts[1]);

        return i * j;
    }
}
