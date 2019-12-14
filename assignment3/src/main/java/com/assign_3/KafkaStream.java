package com.assign_3;

import java.time.Duration;
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
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindowedKStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

        StreamsBuilder builder = new StreamsBuilder();

        // id , 10 10 Spain
        KStream<String, String> salesStream = builder.stream(topicSales);
        KStream<String, String> purchasesStream = builder.stream(topicPurchases);

        // Table with each item grouped by key
        KTable<String, Double> revenueTable = salesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> {
                    return v1 + v2;
                });
        revenueTable.toStream().foreach((k, v) -> {
            System.out.println("Revenue: " + k + " : " + v);
        });
        revenueTable.toStream().map((k, v) -> new KeyValue<>("", tDatabase("revenue", k, v))).to("results",
                Produced.with(Serdes.String(), Serdes.String()));

        // Expenses
        /* KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        expensesTable.toStream().foreach((k, v) -> {
            System.out.println("expenses: " + k + " : " + v);
        });
        expensesTable.toStream().map((k, v) -> new KeyValue<>("", tDatabase("expense", k, v))).to("results",
                Produced.with(Serdes.String(), Serdes.String()));
        // Profit
        KTable<String, String> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            Double x = valueRevennue - valueExpenses;
            return ("" + x);
        });

        profitTable.toStream().map((k, v) -> new KeyValue<>("", tDatabase("profit", k, Double.parseDouble(v))))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Total revenue
        KGroupedStream<String, Double> revenue_group = revenueTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalRevenue = revenue_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        // totalRevenue.toStream().map((k, v) -> new KeyValue<>("",
        // tDatabase("totalRevenue", "0", v))).to("results",
        // Produced.with(Serdes.String(), Serdes.String()));

        // Total expense
        KGroupedStream<String, Double> expense_group = expensesTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalExpense = expense_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        totalExpense.toStream().map((k, v) -> new KeyValue<>("", tDatabase("totalExpense", "0", v))).to("results",
                Produced.with(Serdes.String(), Serdes.String()));

        // Total profit
        KGroupedStream<String, String> profitGroup = profitTable.toStream().groupBy((k, v) -> "");
        KTable<String, String> totalProfit = profitGroup.reduce((v1, v2) -> {
            Double x = Double.parseDouble(v1) + Double.parseDouble(v2);
            return ("" + x);
        });

        totalProfit.toStream().map((k, v) -> new KeyValue<>("", tDatabase("totalProfit", "0", Double.parseDouble(v))))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Timed window for revenue
        TimeWindowedKStream<String, Double> revenueWindow = revenueTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowRev = revenueWindow.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        totalWindowRev.toStream().map((k, v) -> new KeyValue<>("", tDatabase("totalWindowRevenue", "0", v)))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Timed window for expenses
        TimeWindowedKStream<String, Double> expenseWindow = expensesTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowExp = expenseWindow.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        totalWindowExp.toStream().map((k, v) -> new KeyValue<>("", tDatabase("totalWindowExpense", "0", v)))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Timed window for profit

        TimeWindowedKStream<String, String> profitWindow = profitTable.toStream().groupBy((k, v) -> "")
                .windowedBy(TimeWindows.of(TIME_GAP));
        KTable<Windowed<String>, String> totalWindowProfit = profitWindow.reduce((v1, v2) -> {
            Double x = Double.parseDouble(v1) + Double.parseDouble(v2);
            return (v1 + v2);
        });

        totalWindowProfit.toStream()
                .map((k, v) -> new KeyValue<>("", tDatabase("totalWindowProfit", "0", Double.parseDouble(v))))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Purchases Mean per item
        KTable<String, String> medianPerItem = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(() -> "0,0", (id, newVal, aggVal) -> {
                    String parts[] = aggVal.split(",");
                    Double val = Double.parseDouble(parts[0]) + newVal;
                    int count = Integer.parseInt(parts[1]) + 1;
                    return (val + "," + count);
                });

        medianPerItem.toStream().map((k, v) -> new KeyValue<>("", tDatabase("medianPerItem", k, Double.parseDouble(v))))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Total Purchases Mean
        KTable<String, String> medianTotal = purchasesStream.mapValues(v -> transform(v)).groupBy((k, v) -> "")
                .aggregate(() -> "0,0", (id, newVal, aggVal) -> {
                    String parts[] = aggVal.split(",");
                    Double val = Double.parseDouble(parts[0]) + newVal;
                    int count = Integer.parseInt(parts[1]) + 1;
                    return (val + "," + count);
                });

        medianTotal.toStream().map((k, v) -> new KeyValue<>("", tDatabase("totalMedian", "0", Double.parseDouble(v))))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Highest Profit Item

        KTable<String, String> highestProfit = profitTable.toStream().mapValues((k, v) -> k + "," + v)
                .groupBy((k, v) -> "").reduce((v1, v2) -> {
                    String v1parts[] = v1.split(",");
                    String v2parts[] = v2.split(",");

                    if (Double.parseDouble(v1parts[1]) >= Double.parseDouble(v2parts[1]))
                        return v1;
                    else
                        return v2;
                });
        highestProfit.toStream().map((k, v) -> new KeyValue<>("", tDatabase("highestProfit", k, Double.parseDouble(v))))
                .to("results", Produced.with(Serdes.String(), Serdes.String()));

        // Country with Highest Sales

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

        countryHighestSalesSplitted.toStream().foreach((k, v) -> {
            System.out.println("Highest Sales: " + k + " : " + v);
        }); 
        */
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
        // {"schema":{"type":"struct","fields":[{"type":"string","optional":false,"field":"data_type"},{"type":"double","optional":false,"field":"value"},{"type":"double","optional":false,"field":"information_id"}],"optional":false,"name":"total data"},"payload":{"data_type":"profit", "value":10.0,"information_id":1.0}}
        JSONObject json = new JSONObject();
        JSONObject schema_json = new JSONObject();
        JSONObject payload = new JSONObject();
        JSONObject data_type = new JSONObject();
        JSONObject value_json = new JSONObject();
        JSONObject information = new JSONObject();
        JSONArray array = new JSONArray();
        data_type.put("type", "string");
        data_type.put("optional", "false");
        data_type.put("field", "data_type");
        value_json.put("type", "double");
        value_json.put("optional", "false");
        value_json.put("field", "value");
        information.put("type", "double");
        information.put("optional", "false");
        information.put("field", "information_id");
        array.add(data_type);
        array.add(value_json);
        array.add(information);
        schema_json.put("name", "total data");
        schema_json.put("optional", "false");
        schema_json.put("fields", array);
        schema_json.put("type", "struct");

        payload.put("data_type", type);
        payload.put("information_id", Double.parseDouble(id));
        payload.put("value", value);

        json.put("schema", schema_json);
        json.put("payload", payload);
        return json.toString();
    }
}
