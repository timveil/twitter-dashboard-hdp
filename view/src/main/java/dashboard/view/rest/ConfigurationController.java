package dashboard.view.rest;

import dashboard.view.model.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigurationController {

    @RequestMapping(value = "/capture", method = RequestMethod.POST)
    public String capture(@RequestBody Configuration configuration) {

        return "ok";
    }
}
