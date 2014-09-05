package dashboard.kafka;

import dashboard.kafka.config.AppConfig;
import dashboard.kafka.twitter.TweetStreamListener;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;

import java.util.ArrayList;
import java.util.List;

public class Harness {

    private static final Logger log = LoggerFactory.getLogger(Harness.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        Environment environment = ctx.getEnvironment();

        if (log.isDebugEnabled()) {
            log.debug(environment.toString());
        }

        ProducerConfig config = ctx.getBean(ProducerConfig.class);
        Twitter twitter = ctx.getBean(Twitter.class);
        Producer<String, String> producer = new Producer<>(config);

        List<StreamListener> listeners = new ArrayList<>();
        listeners.add(new TweetStreamListener(producer, environment.getProperty("twitter.multiplier", Integer.class)));

        Stream twitterStream = null;

        try {

            twitterStream = twitter.streamingOperations().sample(listeners);

            Thread.sleep(environment.getProperty("twitter.duration", Integer.class));

        } catch (InterruptedException e) {
            log.error("stream thread interrupted...", e);
        } finally {
            log.debug("closing stream");

            if (twitterStream != null) {
                twitterStream.close();
            }
        }

        producer.close();

    }
}
