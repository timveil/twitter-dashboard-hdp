package dashboard.kafka.config;

import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.Properties;

@Configuration
@PropertySource(value = {"classpath:twitter.properties", "classpath:kafka.properties"})
public class AppConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @Bean
    public Twitter twitter() {
        final String consumerKey = environment.getProperty("twitter.consumer-key");
        final String consumerSecret = environment.getProperty("twitter.consumer-secret");
        final String accessToken = environment.getProperty("twitter.access-token");
        final String accessTokenSecret = environment.getProperty("twitter.access-token-secret");

        log.debug("twitter consumer key:  " + consumerKey);
        log.debug("twitter consumer secret:  " + consumerSecret);
        log.debug("twitter access token:  " + accessToken);
        log.debug("twitter access token secret:  " + accessTokenSecret);

        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    @Bean
    public ProducerConfig producerConfig() {
        Properties props = new Properties();
        props.put("metadata.broker.list", environment.getProperty("metadata.broker.list"));
        props.put("serializer.class", environment.getProperty("serializer.class"));
        props.put("key.serializer.class", environment.getProperty("key.serializer.class"));
        props.put("request.required.acks", environment.getProperty("request.required.acks"));
        props.put("producer.type", environment.getProperty("producer.type"));

        return new ProducerConfig(props);
    }

}
