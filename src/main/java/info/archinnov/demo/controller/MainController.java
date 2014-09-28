package info.archinnov.demo.controller;

import java.util.List;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import info.archinnov.demo.model.RateLimit;
import info.archinnov.demo.service.DbService;

@Controller
@RequestMapping(produces = "application/json")
public class MainController {

    @Inject
    private DbService dbService;

    @RequestMapping(value = "/timestamp", method = RequestMethod.GET)
    @ResponseBody
    public String getCurrentTime() {
        return dbService.getCurrentTime();
    }

    @RequestMapping(value = "/ratelimit/threshold/{threshold}", method = RequestMethod.PUT)
    @ResponseBody
    public void setThresholdForRateLimit(@PathVariable("threshold") int threshold) {
        dbService.setThresholdForRateLimit(threshold);
    }

    @RequestMapping(value = "/ratelimit/{value}/{ttl}", method = RequestMethod.PUT)
    @ResponseBody
    public void insertForRateLimit(@PathVariable("value") String value, @PathVariable("ttl") int ttl) {
        dbService.insertForRateLimit(value, ttl);
    }

    @RequestMapping(value = "/ratelimits", method = RequestMethod.GET)
    @ResponseBody
    public List<RateLimit> insertForRateLimit() {
        return dbService.fetchRateLimitedValues();
    }



    @RequestMapping(value = "/db", method = RequestMethod.DELETE)
    @ResponseBody
    public String resetDb() {
        dbService.resetDb();
        return "Data reset successfully";
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(IllegalStateException ex) {
        return ex.getMessage();
    }

}
