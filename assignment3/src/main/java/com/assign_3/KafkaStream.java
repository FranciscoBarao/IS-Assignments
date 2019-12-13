package com.assign_3;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SessionWindowedKStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Windowed;

public class KafkaStream {
    static final long TIME_GAP = Duration.ofMinutes(2).toMillis();

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

        // id , 10 10 Spain
        KStream<String, String> salesStream = builder.stream(topicSales);
        KStream<String, String> purchasesStream = builder.stream(topicPurchases);

        // Table with each item grouped by key
        KTable<String, Double> revenueTable = salesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> {
                    return v1 + v2;
                });

        revenueTable.toStream().map((k, v) -> new KeyValue<String, String>("", tDatabase("revenue", k, v)))
                .to("itemTopic", Produced.with(Serdes.String(), Serdes.String()));

        KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        KTable<String, Double> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            return (valueRevennue - valueExpenses);
        });

        // Total revenue/profit/expense
        KGroupedStream<String, Double> revenue_group = revenueTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalRevenue = revenue_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        totalRevenue.toStream().foreach((k, v) -> {
            System.out.println("Total revenue: " + k + " " + v);
        });

        KGroupedStream<String, Double> expense_group = expensesTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalExpense = expense_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        totalExpense.toStream().foreach((k, v) -> {
            System.out.println("Total expense: " + k + " " + v);
        });

        KGroupedStream<String, Double> profit_group = profitTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalProfit = profit_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" +
        // v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Grouped by time
        SessionWindowedKStream<String, Double> revenue_window = revenueTable.toStream().groupBy((k, v) -> "")
                .windowedBy(SessionWindows.with(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowRev = revenue_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        SessionWindowedKStream<String, Double> expense_window = expensesTable.toStream().groupBy((k, v) -> "")
                .windowedBy(SessionWindows.with(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowExp = expense_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        totalWindowRev.toStream().foreach((k, v) -> {
            // System.out.println("Total Window something: " + k + " " + v);
        });

        // totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" +
        // v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

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

        // Calcular medias -> aggregate - fazer soma e contar quantos ja ocorreram ...
        // ("total, counter")
        // agreggate passa id, value,"total,count"

        KTable<String, String> medianPerItem = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(() -> "0,0", (id, newVal, aggVal) -> {
                    String parts[] = aggVal.split(",");
                    Double val = Double.parseDouble(parts[0]) + newVal;
                    int count = Integer.parseInt(parts[1]) + 1;
                    return (val + "," + count);
                });

        KTable<String, String> medianTotal = purchasesStream.mapValues(v -> transform(v)).groupBy((k, v) -> "")
                .aggregate(() -> "0,0", (id, newVal, aggVal) -> {
                    String parts[] = aggVal.split(",");
                    Double val = Double.parseDouble(parts[0]) + newVal;
                    int count = Integer.parseInt(parts[1]) + 1;
                    return (val + "," + count);
                });
    }

    private static Double transform(String s) {
        String parts[] = s.split(" ");
        Double i = Double.parseDouble(parts[0]);
        Double j = Double.parseDouble(parts[1]);

        return i * j;
    }

    public static String tDatabase(String type, String id, Double value) {
        return "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"data_type\"},{\"type\":\"double\",\"optional\":false,\"field\":\"value\"},{\"type\":\"integer\",\"optional\":false,\"field\":\"item_id\"}],\"optional\":false,\"name\":\"total data\"},\"payload\":{\"data_type\":\""
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
