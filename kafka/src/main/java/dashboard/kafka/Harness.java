package dashboard.kafka;

import com.google.common.collect.ImmutableList;
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
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.text.NumberFormat;
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

        Integer durationInMinutes = environment.getProperty("twitter.ingest.duration.minutes", Integer.class);
        Integer durationInMilliseconds = durationInMinutes * 60 * 1000;

        if (log.isDebugEnabled()) {
            log.debug("app will ingest twitter data for " + NumberFormat.getNumberInstance().format(durationInMilliseconds) + " milliseconds or " + durationInMinutes + " minutes!");
        }

        ProducerConfig config = ctx.getBean(ProducerConfig.class);
        Twitter twitter = ctx.getBean(Twitter.class);

        Producer<String, Tweet> producer = new Producer<>(config);

        List listeners = ImmutableList.of(new TweetStreamListener(producer));

        Stream twitterStream = null;

        ConsoleReporter reporter = new ConsoleReporter(Metrics.defaultRegistry(), System.out, MetricPredicate.ALL);
        reporter.start(1, TimeUnit.MINUTES);

        try {

            FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
            // San Francisco
            filterStreamParameters.addLocation(-122.75f,36.8f,-121.75f,37.8f);
            // New York
            filterStreamParameters.addLocation(-74,40,-73,41);

            twitterStream = twitter.streamingOperations().filter(filterStreamParameters, listeners);

            Thread.sleep(durationInMilliseconds);

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
