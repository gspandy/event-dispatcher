package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.riskverify.Handler;
import com.ctrip.infosec.riskverify.Receiver;
import com.google.common.collect.ImmutableMap;
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
    private final String FACT = "REST";
    @Autowired
    private Handler handler;

    @Override
    public void init() {
        throw new RuntimeException("过期方法");
    }

    @RequestMapping(value = "/riskverify", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<RiskResult> riskverify(@RequestBody RiskFact req) {
        RiskResult riskResult = handler.send(ImmutableMap.of("FACT", FACT, "CP", req.getEventPoint(), "body", req));
        return new ResponseEntity<RiskResult>(riskResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public
    @ResponseBody
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

    @Override
    public void recovery() {

    }
}
