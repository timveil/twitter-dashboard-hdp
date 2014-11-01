package dashboard.view.service;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.reporting.ConsoleReporter;
import dashboard.common.kafka.TweetStreamListener;
import dashboard.view.model.Configuration;
import dashboard.view.model.IngestStatus;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TweetStreamServiceImpl implements TweetStreamService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProducerConfig config;

    @Autowired
    private Twitter twitter;

    private Stream stream;

    private Producer<String, Tweet> producer;

    private AtomicReference<IngestStatus> status = new AtomicReference<>(new IngestStatus(false));

    private ConsoleReporter reporter;


    @Override
    public IngestStatus startIngest(Configuration configuration) {

        IngestStatus ingestStatus = status.get();

        if (!ingestStatus.isRunning()) {

            log.info("starting ingest");

            producer = new Producer<>(config);

            List listeners = ImmutableList.of(new TweetStreamListener(producer));

            ingest(configuration, listeners);

            ingestStatus.setRunning(true);
            ingestStatus.setStarted(new Date());
            status.set(ingestStatus);

            if (reporter == null) {
                reporter = new ConsoleReporter(Metrics.defaultRegistry(), System.out, new MetricPredicate() {
                    @Override
                    public boolean matches(MetricName name, Metric metric) {
                        return name.getName().contains("AllTopicsMessagesPerSec");
                    }
                });
            }

            reporter.start(30, TimeUnit.SECONDS);

        } else {
            log.warn("ingest is already started!");
        }

        return ingestStatus;

    }

    @Override
    public IngestStatus stopIngest() {

        IngestStatus ingestStatus = status.get();

        if (ingestStatus.isRunning()) {

            log.info("stopping ingest");

            stream.close();
            producer.close();

            if (reporter != null) {
                reporter.shutdown();
            }

            ingestStatus.setRunning(false);
            ingestStatus.setStarted(null);
            status.set(ingestStatus);

        } else {
            log.warn("ingest is not started!");
        }

        return ingestStatus;
    }

    @Override
    public IngestStatus ingestStatus() {
        return status.get();
    }

    /**
     * // first time hit capture
     * start capture and wait;
     * <p/>
     * // user hist capture again for first is finished
     * finish first and start second
     */

    private void ingest(Configuration configuration, List listeners) {


        List<String> userIds = null;

        if (!Strings.isNullOrEmpty(configuration.getUsers())) {
            userIds = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(configuration.getUsers());
        }

        List<String> locations = null;

        if (!Strings.isNullOrEmpty(configuration.getLocations())) {
            locations = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(configuration.getLocations());
        }

        List<String> phrases = null;

        if (!Strings.isNullOrEmpty(configuration.getPhrases())) {
            phrases = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(configuration.getPhrases());
        }

        if (userIds == null && locations == null && phrases == null) {
            if (log.isDebugEnabled()) {
                log.debug("listening to unfiltered sample stream");
            }

            stream = twitter.streamingOperations().sample(listeners);
        } else {
            FilterStreamParameters filterStreamParameters = new FilterStreamParameters();

            if (userIds != null) {
                for (String userId : userIds) {
                    filterStreamParameters.follow(Integer.parseInt(userId));
                }
            }

            if (phrases != null) {
                for (String phrase : phrases) {
                    filterStreamParameters.track(phrase);
                }
            }

            if (locations != null) {
                List<List<String>> partitions = Lists.partition(locations, 4);

                for (List<String> location : partitions) {
                    filterStreamParameters.addLocation(
                            Float.parseFloat(location.get(0)),
                            Float.parseFloat(location.get(1)),
                            Float.parseFloat(location.get(2)),
                            Float.parseFloat(location.get(3)));
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("listening to filtered stream");
            }

            stream = twitter.streamingOperations().filter(filterStreamParameters, listeners);
        }

    }


}
