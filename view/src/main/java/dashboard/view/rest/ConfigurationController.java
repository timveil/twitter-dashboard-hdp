package dashboard.view.rest;

import dashboard.view.model.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class ConfigurationController {

    @RequestMapping(value = "/capture", method = RequestMethod.POST)
    public String capture(@RequestBody Configuration configuration) {
        return "ok";
    }

    @RequestMapping(value = "/capture", method = RequestMethod.GET)
    public String captureList() {
        return "oklisting";
    }
}
