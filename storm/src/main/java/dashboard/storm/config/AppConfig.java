package dashboard.storm.config;

import backtype.storm.spout.SchemeAsMultiScheme;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource(value = {"classpath:storm.properties", "classpath:kafka.properties"})
public class AppConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @Bean
    public SpoutConfig producerConfig() {

        List<String> zookeepers = Arrays.asList(environment.getProperty("spout.zookeeper.hosts").split(","));

        BrokerHosts hosts = new ZkHosts(Joiner.on(",").join(zookeepers));

        SpoutConfig config = new SpoutConfig(
                hosts,
                environment.getProperty("spout.topic"),
                environment.getProperty("spout.zookeeper.root"),
                environment.getProperty("spout.id")
        );

        config.scheme = new SchemeAsMultiScheme(new StringScheme());
        config.zkPort = Integer.parseInt(environment.getProperty("spout.zookeeper.port"));
        config.zkServers = zookeepers;

        return config;

    }


}
