package dashboard.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import dashboard.storm.bolts.TweetBolt;
import dashboard.storm.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.bolt.KafkaBolt;

import java.util.HashMap;

public class Harness {

    private static final Logger log = LoggerFactory.getLogger(Harness.class);

    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        final Environment environment = ctx.getEnvironment();

        if (log.isDebugEnabled()) {
            log.debug(environment.toString());
        }

        KafkaSpout kafkaSpout = new KafkaSpout(ctx.getBean(SpoutConfig.class));

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(environment.getProperty("spout.name"), kafkaSpout);

        BoltDeclarer boltDeclarer = topologyBuilder.setBolt(environment.getProperty("bolt.tweet.name"), new TweetBolt());
        boltDeclarer.shuffleGrouping(environment.getProperty("spout.name"));

        Config stormConfig = buildStormConfig(environment);

        stormConfig.put(KafkaBolt.KAFKA_BROKER_PROPERTIES, new HashMap<String, String>() {{
            put("metadata.broker.list", environment.getProperty("metadata.broker.list"));
            put("serializer.class", environment.getProperty("serializer.class"));
            put("request.required.acks", environment.getProperty("request.required.acks"));
            put("producer.type", environment.getProperty("producer.type"));
        }});

        try {
            StormSubmitter.submitTopology(environment.getProperty("storm.topology.name"), stormConfig, topologyBuilder.createTopology());
        } catch (AlreadyAliveException | InvalidTopologyException e) {
            log.error("error building or submitting topology", e);
        }


    }

    private static Config buildStormConfig(Environment environment) {
        Config stormConfig = new Config();
        stormConfig.setDebug(Boolean.parseBoolean(environment.getProperty("storm.debug")));
        stormConfig.setMaxSpoutPending(Integer.parseInt(environment.getProperty("storm.max.pending")));
        stormConfig.setNumWorkers(Integer.parseInt(environment.getProperty("storm.num.workers")));
        stormConfig.setMessageTimeoutSecs(Integer.parseInt(environment.getProperty("storm.message.timeout.secs")));
        return stormConfig;
    }


}
