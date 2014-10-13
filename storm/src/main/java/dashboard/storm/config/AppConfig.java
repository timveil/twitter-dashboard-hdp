package dashboard.storm.config;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import dashboard.storm.bolts.SolrBolt;
import dashboard.storm.bolts.TweetScheme;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
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

        config.scheme = new SchemeAsMultiScheme(new TweetScheme(objectMapper()));
        config.zkPort = Integer.parseInt(environment.getProperty("spout.zookeeper.port"));
        config.zkServers = zookeepers;

        return config;

    }

    @Bean
    public HdfsBolt buildHdfsBolt() {
        RecordFormat format = new DelimitedRecordFormat().withFields(new Fields(TweetScheme.TWEET_AS_JSON)).withFieldDelimiter("|");

        SyncPolicy syncPolicy = new CountSyncPolicy(1000);

        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f, FileSizeRotationPolicy.Units.MB);

        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath(environment.getProperty("bolt.hdfs.path"));

        HdfsBolt bolt = new HdfsBolt()
                .withFsUrl(environment.getProperty("bolt.hdfs.fs.url"))
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);


        return bolt;
    }

    @Bean
    public SolrBolt buildSolrBolt() {
        return new SolrBolt(environment.getProperty("bolt.solr.server.url"));
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
