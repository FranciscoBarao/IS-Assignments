package com.assign_3;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
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

        // id , 10 10 Spain
        KStream<String, String> salesStream = builder.stream(topicSales);
        KStream<String, String> purchasesStream = builder.stream(topicPurchases);

        // Table with each item grouped by key
        KTable<String, Double> revenueTable = salesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> {
                    return v1 + v2;
                });

        // Expenses
        KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        // Profit
        KTable<String, Double> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            return (valueRevennue - valueExpenses);
        });

        // Total revenue
        KGroupedStream<String, Double> revenue_group = revenueTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalRevenue = revenue_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // Total expense
        KGroupedStream<String, Double> expense_group = expensesTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalExpense = expense_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // Total profit
        KGroupedStream<String, Double> profit_group = profitTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalProfit = profit_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // Timed window for revenue
        TimeWindowedKStream<String, Double> revenue_window = revenueTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowRev = revenue_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // Timed window for expenses
        TimeWindowedKStream<String, Double> expense_window = expensesTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowExp = expense_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // Mean per item
        KTable<String, String> medianPerItem = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(() -> "0,0", (id, newVal, aggVal) -> {
                    String parts[] = aggVal.split(",");
                    Double val = Double.parseDouble(parts[0]) + newVal;
                    int count = Integer.parseInt(parts[1]) + 1;
                    return (val + "," + count);
                });

        // Total Mean
        KTable<String, String> medianTotal = purchasesStream.mapValues(v -> transform(v)).groupBy((k, v) -> "")
                .aggregate(() -> "0,0", (id, newVal, aggVal) -> {
                    String parts[] = aggVal.split(",");
                    Double val = Double.parseDouble(parts[0]) + newVal;
                    int count = Integer.parseInt(parts[1]) + 1;
                    return (val + "," + count);
                });

        KTable<String, String> highestProfit = profitTable.toStream().mapValues((k, v) -> k + "," + v)
                .groupBy((k, v) -> "").reduce((v1, v2) -> {
                    String v1parts[] = v1.split(",");
                    String v2parts[] = v2.split(",");

                    if (Double.parseDouble(v1parts[1]) >= Double.parseDouble(v2parts[1]))
                        return v1;
                    else
                        return v2;
                });

        KTable<String, String> countryHighestSalesSplitted = salesStream.groupBy((k, v) -> {
            String parts[] = v.split(" ");
            return (k + "," + parts[2]);
        }).reduce((v1, v2) -> {
            String v1parts[] = v1.split(" ");
            String v2parts[] = v2.split(" ");

            Double x = Double.parseDouble(v1parts[0]) * Double.parseDouble(v1parts[1])
                    + Double.parseDouble(v2parts[0]) * Double.parseDouble(v2parts[1]);
            return ("" + x);
        });

        // totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" +
        // v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        // totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" +
        // v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        // revenueTable.toStream().map((k, v) -> new KeyValue<String, String>("",
        // tDatabase("revenue", k, v)))
        // .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Properties for streams
        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
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
}
