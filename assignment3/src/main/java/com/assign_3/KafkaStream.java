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
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
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

        KStream<String, String> salesStream = builder.stream(topicSales);
        // id , 10 10 Spain
        KStream<String, String> purchasesStream = builder.stream(topicPurchases);

        KTable<String, Double> revenueTable = salesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> {
                    return v1 + v2;
                });
        revenueTable.toStream().map((k, v) -> new KeyValue<>(k, "" + v)).to(outputTopic,
                Produced.with(Serdes.String(), Serdes.String()));

        KTable<String, Double> expensesTable = purchasesStream.mapValues(v -> transform(v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double())).reduce((v1, v2) -> v1 + v2);

        KTable<String, Double> profitTable = revenueTable.join(expensesTable, (valueRevennue, valueExpenses) -> {
            return (valueRevennue - valueExpenses);
        });

        KGroupedStream<String, Double> revenue_group = revenueTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalRevenue = revenue_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        KGroupedStream<String, Double> expense_group = expensesTable.toStream().groupBy((k, v) -> "");
        KTable<String, Double> totalExpense = expense_group.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        //totalRevenue.toStream().map((k, v) -> new KeyValue<>(k, "" + v)).to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        // Grouped by time
        SessionWindowedKStream<String,Double> revenue_window = revenueTable.toStream().groupBy((k, v) -> "").windowedBy(SessionWindows.with(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowRev = revenue_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });

        SessionWindowedKStream<String,Double> expense_window = expensesTable.toStream().groupBy((k, v) -> "").windowedBy(SessionWindows.with(TIME_GAP));
        KTable<Windowed<String>, Double> totalWindowExp = expense_window.reduce((v1, v2) -> {
            return (v1 + v2);
        });
        totalRevenue.toStream().foreach((k, v) -> {
            System.out.println("Total something: " + k + " " + v);
        });

        totalWindowRev.toStream().foreach((k, v) -> {
            System.out.println("Total Window something: " + k + " " + v);
        });


        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        // Total profit needs a groupBy(k,v -> null)
        /* final KTable<String,Tuple2<Long,Long>> countAndSum = revenue_group
                                                            .aggregate(
                                                                new Initializer<Tuple2<Long, Long>>() {
                                                                    @Override
                                                                    public Tuple2<Long, Long> apply() {
                                                                        return new Tuple2<>(0L, 0L);
                                                                    }
                                                                },
                                                                new Aggregator<String, String, Tuple2<Long, Long>>() {
                                                                    @Override
                                                                    public Tuple2<Long, Long> apply(final String key, final Long value, final Tuple2<Long, Long> aggregate) {
                                                                            ++aggregate.value1;
                                                                            aggregate.value2 += value;
                                                                            return aggregate;
                                                                    }
                                                                }   );
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

    class Tuple2<T1, T2> {
        public T1 value1;
        public T2 value2;
        
        Tuple2(T1 v1, T2 v2) {
            value1 = v1;
            value2 = v2;
        }
    }
}
