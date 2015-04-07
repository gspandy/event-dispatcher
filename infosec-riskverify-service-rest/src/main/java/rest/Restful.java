package rest;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.event.Channel;
import com.google.common.collect.ImmutableMap;
import enums.InnerEnum;
import handlerImpl.Handler;
import manager.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangsx on 2015/2/3.
 */
@Controller
public class Restful implements Receiver {
//    private final String FACT = "REST";

    @Autowired
    private Handler handler;

    @RequestMapping(value = "/riskverify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RiskResult> riskverify(@RequestBody RiskFact fact) {

        RiskResult riskResult = handler.send(Channel.REST, fact);
        return new ResponseEntity<RiskResult>(riskResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public @ResponseBody
    String checkHealth() {
        return "hello!";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

}
