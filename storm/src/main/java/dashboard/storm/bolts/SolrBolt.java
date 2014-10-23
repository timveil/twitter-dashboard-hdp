package dashboard.storm.bolts;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.MentionEntity;
import org.springframework.social.twitter.api.Tweet;

import java.io.IOException;
import java.util.Map;

public class SolrBolt extends BaseBasicBolt {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private enum Type {
        tweet, hashtag, mention
    }

    private SolrServer server = null;

    private String serverUrl = null;

    public SolrBolt(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        server = new HttpSolrServer(serverUrl);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Tweet tweet = (Tweet) input.getValueByField(TweetScheme.TWEET);
        index(tweet);
    }

    private void index(Tweet tweet) {
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField("id", tweet.getId() + ":" + Type.tweet.toString());
        doc.addField("doctype", Type.tweet.toString());
        doc.addField("createdAt", tweet.getCreatedAt());
        doc.addField("text", tweet.getText());
        doc.addField("language", tweet.getLanguageCode());
        doc.addField("screenName", tweet.getUser().getScreenName());
        doc.addField("source", tweet.getSource());
        doc.addField("location", tweet.getUser().getLocation());

        addDocument(doc);

        if (tweet.hasTags()) {

            for (HashTagEntity entity : tweet.getEntities().getHashTags()) {

                if (StringUtils.isNotBlank(entity.getText())) {

                    SolrInputDocument hashtagDoc = new SolrInputDocument();
                    hashtagDoc.addField("id",  tweet.getId() + ":" + Type.hashtag.toString());
                    hashtagDoc.addField("doctype", Type.hashtag.toString());
                    hashtagDoc.addField("createdAt", tweet.getCreatedAt());
                    hashtagDoc.addField("text", entity.getText());

                    addDocument(hashtagDoc);

                }
            }

        }

        if (tweet.hasMentions()) {

            for (MentionEntity entity : tweet.getEntities().getMentions()) {

                if (StringUtils.isNotBlank(entity.getScreenName())) {

                    SolrInputDocument mentionDoc = new SolrInputDocument();
                    mentionDoc.addField("id", tweet.getId() + ":" + Type.mention.toString());
                    mentionDoc.addField("doctype", Type.mention.toString());
                    mentionDoc.addField("createdAt", tweet.getCreatedAt());
                    mentionDoc.addField("screenName", entity.getScreenName());
                    mentionDoc.addField("name", entity.getName());

                    addDocument(mentionDoc);

                }
            }

        }



    }

    private void addDocument(SolrInputDocument doc) {
        try {
            server.add(doc);
        } catch (IOException | SolrServerException e) {
            log.error("error indexing tweet", e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // no-op
    }
}
