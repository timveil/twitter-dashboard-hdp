package dashboard.storm.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

public class TweetBolt extends BaseBasicBolt {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Tweet tweet = (Tweet) input.getValueByField(TweetScheme.TWEET);

        if (log.isDebugEnabled()) {
            log.debug("tweetId: " + tweet.getId() + ", text: " + tweet.getText());
        }

        collector.emit(new Values(input.getValueByField(TweetScheme.TWEET), input.getValueByField(TweetScheme.TWEET_AS_JSON)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(TweetScheme.TWEET, TweetScheme.TWEET_AS_JSON));
    }
}
