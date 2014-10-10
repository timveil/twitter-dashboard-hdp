package dashboard.storm.bolts;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dashboard.common.kafka.TweetEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.util.List;

public class TweetScheme implements Scheme {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String TWEET = "tweet";
    public static final String TWEET_AS_JSON = "tweet_as_json";

    private ObjectMapper objectMapper;

    public TweetScheme(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Object> deserialize(byte[] ser) {
        Tweet tweet = TweetEncoder.getTweet(ser);
        try {
            return new Values(tweet, objectMapper.writeValueAsString(tweet));
        } catch (JsonProcessingException e) {
            log.error("error processing as json");
        }

        return null;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields(TWEET, TWEET_AS_JSON);
    }
}
