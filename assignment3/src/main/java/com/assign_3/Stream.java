package com.assign_3;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class Stream {

    public static void main(String[] args) throws InterruptedException, IOException {
        String topicName = "pre-test";
        String outtopicname = "test";

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "exercises-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
            
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> lines = builder.stream(topicName);

        KTable<String, String> outlines = lines.mapValues(v->transform(v)).
        groupByKey((GRouped.with(Serdes.String(),Serdes.Double()).reduce((v1,v2) ->
        v1+v2));
        outlines.toStream().to(outtopicname);
        

/*
        KTable<String, Long> outlines = lines.
    groupByKey().
    reduce((oldval, newval) -> oldval + newval);
outlines.mapValues((k, v) -> k + " => " + v).toStream().to(outtopicname, Produced.with(Serdes.String(), Serdes.String()));
*/
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        System.out.println("Reading stream from topic " + topicName);
        
    }

    public static String transform(String x) {

    }
}