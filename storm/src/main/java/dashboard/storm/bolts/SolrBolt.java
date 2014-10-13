package dashboard.storm.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.io.IOException;
import java.util.Map;

public class SolrBolt implements IRichBolt {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private SolrServer server = null;

    private String serverUrl = null;

    public SolrBolt(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        server = new HttpSolrServer(serverUrl);
    }

    @Override
    public void execute(Tuple input) {
        Tweet tweet = (Tweet) input.getValueByField(TweetScheme.TWEET);
        index(tweet);
    }


    private void index(Tweet tweet) {
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField("id", tweet.getId());
        doc.addField("createdAt", tweet.getCreatedAt());
        doc.addField("text", tweet.getText());
        doc.addField("screenName", tweet.getUser().getScreenName());

        try {
            server.add(doc);
            server.commit();
        } catch (IOException | SolrServerException e) {
            log.error("error indexing tweet", e);
        }
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
