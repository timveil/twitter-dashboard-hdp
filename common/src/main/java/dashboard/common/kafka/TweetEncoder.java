package dashboard.common.kafka;

import kafka.serializer.Decoder;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.io.*;

public class TweetEncoder implements Encoder<Tweet>, Decoder<Tweet> {

    private static final Logger log = LoggerFactory.getLogger(TweetEncoder.class);

    public TweetEncoder(VerifiableProperties verifiableProperties) {
    }

    @Override
    public byte[] toBytes(Tweet tweet) {
        return getBytes(tweet);
    }

    @Override
    public Tweet fromBytes(byte[] bytes) {
        return getTweet(bytes);
    }

    public static Tweet getTweet(byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try (ObjectInputStream is = new ObjectInputStream(in)) {
            return (Tweet) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("error converting tweet from bytes", e);
        }

        return null;
    }

    public static byte[] getBytes(Tweet tweet) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); ObjectOutputStream os = new ObjectOutputStream(out)) {
            os.writeObject(tweet);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("error converting tweet to bytes", e);
        }
        return null;
    }
}
