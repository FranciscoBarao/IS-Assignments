package com.assign_3;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

public class KafkaStream {
    /*
     * Thread for producer Talk to prof about parsing info from DB and to Send to
     * other kafka streams
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Kafka Stream");
        // Kafka consumer configuration settings
        String topicSales = "Sales";
        String topicPurchases = "Purchases";
        String outputTopic = "output";

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> salesStream = builder.stream(topicSales);
        // id , 10 10 Spain
        KStream<String, String> purchasesStream = builder.stream(topicPurchases);

        KTable<String, Double> revenueTable = salesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> {
                    return v1 + v2;
                });

        revenueTable.toStream().map((k, v) -> new KeyValue<>("", test("revenue", k, v))).to("itemTopic",
                Produced.with(Serdes.String(), Serdes.String()));

        KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        KTable<String, Double> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            return (valueRevennue - valueExpenses);
        });

        KGroupedStream<String, Double> revenueGroupStream = revenueTable.toStream().groupBy((v1, v2) -> null);
        KTable<String, Double> totalRevenue = revenueGroupStream.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        /*
         * KTable<String, Double> medianExpenseItem = purchasesStream.mapValues(v ->
         * transform(v)) .groupByKey(Grouped.with(Serdes.String(),
         * Serdes.Double())).reduce((v1, v2) -> v1 + v2);
         * 
         * KTable<String, Double> medianRevenueItem = revenueTable.toStream()
         * 
         * KTable<String, Double> medianRevenue = revenueGroupStream.aggregate(() -> 0L,
         * (value, total, count) -> total + value, Materialized.as("count")
         * .withValueSerde(Serdes.Double()));
         */

        // ("total, counter")
        // agreggate passa id, value,"total,count"

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
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

    private static String test(String type, String id, Double value) {
        return "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"data_type\"},{\"type\":\"double\",\"optional\":false,\"field\":\"value\"},{\"type\":\"int\",\"optional\":false,\"field\":\"item_id\"}],\"optional\":false,\"name\":\"total data\"},\"payload\":{\"type\":\""
                + type + "\", \"value\":\"" + value + "\",\"item_id\":\"" + id + "\"}}";
    }
}
