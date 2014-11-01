package dashboard.view.rest;

import dashboard.view.model.Configuration;
import dashboard.view.model.IngestStatus;
import dashboard.view.service.TweetStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class ConfigurationController {

    @Autowired
    private TweetStreamService tweetStreamService;

    @RequestMapping(value = "/ingest/start", method = RequestMethod.POST)
    public IngestStatus start(@RequestBody Configuration configuration) {
        return tweetStreamService.startIngest(configuration);
    }

    @RequestMapping(value = "/ingest/stop", method = RequestMethod.POST)
    public IngestStatus stop(@RequestBody Configuration configuration) {
        return tweetStreamService.stopIngest();
    }

    @RequestMapping(value = "/ingest/status", method = RequestMethod.GET)
    public IngestStatus status() {
        return tweetStreamService.ingestStatus();
    }
}
