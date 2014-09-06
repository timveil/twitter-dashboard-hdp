package dashboard.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetBolt extends BaseBasicBolt {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        log.debug(input.toString());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
