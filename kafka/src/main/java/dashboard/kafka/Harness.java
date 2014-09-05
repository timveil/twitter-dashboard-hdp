package dashboard.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.reporting.ConsoleReporter;
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
import org.springframework.social.twitter.api.Twitter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Harness {

    private static final Logger log = LoggerFactory.getLogger(Harness.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        Environment environment = ctx.getEnvironment();

        if (log.isDebugEnabled()) {
            log.debug(environment.toString());
        }

        Integer duration = environment.getProperty("twitter.ingest.duration.milliseconds", Integer.class);

        if (log.isDebugEnabled()) {
            log.debug("app will ingest twitter data for " + duration + " milliseconds!");
        }

        ProducerConfig config = ctx.getBean(ProducerConfig.class);
        Twitter twitter = ctx.getBean(Twitter.class);
        ObjectMapper mapper = ctx.getBean(ObjectMapper.class);

        Producer<String, String> producer = new Producer<>(config);

        List listeners = ImmutableList.of(new TweetStreamListener(producer, mapper));

        Stream twitterStream = null;

        ConsoleReporter reporter = new ConsoleReporter(Metrics.defaultRegistry(), System.out, MetricPredicate.ALL);
        reporter.start(5, TimeUnit.MINUTES);

        try {

            twitterStream = twitter.streamingOperations().sample(listeners);

            Thread.sleep(duration);

        } catch (InterruptedException e) {
            log.error("stream thread interrupted...", e);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("closing stream");
            }

            if (twitterStream != null) {
                twitterStream.close();
            }
        }

        reporter.shutdown();
        producer.close();

        System.exit(0);

    }
}
