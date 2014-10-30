package dashboard.view.rest;

import dashboard.view.model.Configuration;
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

    @RequestMapping(value = "/capture", method = RequestMethod.POST)
    public String capture(@RequestBody Configuration configuration) {

        tweetStreamService.ingest(configuration);

        return "ok";
    }

    @RequestMapping(value = "/capture", method = RequestMethod.GET)
    public String captureList() {
        return "oklisting";
    }
}
