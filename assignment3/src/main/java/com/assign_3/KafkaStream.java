package com.assign_3;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SessionWindowedKStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.TimeWindowedKStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

public class KafkaStream {
    static final Duration TIME_GAP = Duration.ofMinutes(5);

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

        revenueTable.toStream().foreach((k, v) -> {
            System.out.println("revenue something: " + k + " " + v);
        });

        //revenueTable.toStream().map((k, v) -> new KeyValue<String, String>("", tDatabase("revenue", k, v)))
        //        .to("results", Produced.with(Serdes.String(), Serdes.String()));

        KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        expensesTable.toStream().foreach((k, v) -> {
            System.out.println("expenses something: " + k + " " + v);
        });

        KTable<String, Double> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            return (valueRevennue - valueExpenses);
        });

        profitTable.toStream().foreach((k, v) -> {
            System.out.println("profit something: " + k + " " + v);
        });

        KGroupedStream<String, Double> revenue_group = revenueTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalRevenue = revenue_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        KGroupedStream<String, Double> expense_group = expensesTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalExpense = expense_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        // totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" +
        // v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Grouped by time
        TimeWindowedKStream<String, Double> revenue_window = revenueTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowRev = revenue_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        TimeWindowedKStream<String, Double> expense_window = expensesTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowExp = expense_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        /* totalRevenue.toStream().foreach((k, v) -> {
            System.out.println("Total something: " + k + " " + v);
        });

        totalWindowRev.toStream().foreach((k, v) -> {
            System.out.println("Total Window something: " + k + " " + v);
        }); */

        // totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" +
        // v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

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
         * final KTable<String,Tuple2<Long,Long>> countAndSum = revenue_group
         * .aggregate( new Initializer<Tuple2<Long, Long>>() {
         * 
         * @Override public Tuple2<Long, Long> apply() { return new Tuple2<>(0L, 0L); }
         * }, new Aggregator<String, String, Tuple2<Long, Long>>() {
         * 
         * @Override public Tuple2<Long, Long> apply(final String key, final Long value,
         * final Tuple2<Long, Long> aggregate) { ++aggregate.value1; aggregate.value2 +=
         * value; return aggregate; } } );
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

    public static String tDatabase(String type, String id, Double value) {
        return "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"data_type\"},{\"type\":\"double\",\"optional\":false,\"field\":\"value\"},{\"type\":\"int\",\"optional\":false,\"field\":\"item_id\"}],\"optional\":false,\"name\":\"total data\"},\"payload\":{\"data_type\":\""
                + type + "\", \"value\":\"" + value + "\",\"item_id\":\"" + id + "\"}}";
    }

    class Tuple2<T1, T2> {
        public T1 value1;
        public T2 value2;

        Tuple2(T1 v1, T2 v2) {
            value1 = v1;
            value2 = v2;
        }
    }
}
