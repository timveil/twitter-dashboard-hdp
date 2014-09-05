package dashboard.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import com.google.common.collect.ImmutableList;
import dashboard.storm.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class Harness {

    private static final Logger log = LoggerFactory.getLogger(Harness.class);

    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        Environment environment = ctx.getEnvironment();

        if (log.isDebugEnabled()) {
            log.debug(environment.toString());
        }


        TopologyBuilder topologyBuilder = new TopologyBuilder();

        KafkaSpout kafkaSpout = new KafkaSpout(ctx.getBean(SpoutConfig.class));

        topologyBuilder.setSpout("kafka-tweets", kafkaSpout);

        Config stormConfig = new Config();

        try {
            StormSubmitter.submitTopology("tweet-processor", stormConfig, topologyBuilder.createTopology());
        } catch (AlreadyAliveException | InvalidTopologyException e) {
            log.error("error", e);
        }

    }


}
