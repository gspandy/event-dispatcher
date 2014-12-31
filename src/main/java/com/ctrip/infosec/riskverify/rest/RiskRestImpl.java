package com.ctrip.infosec.riskverify.rest;

import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.entity.RiskVerifyRequest;
import com.ctrip.infosec.riskverify.entity.RiskVerifyResponse;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhangsx on 2014/12/22.
 */
@Controller
public class RiskRestImpl {
    @Autowired
    private RiskVerifyBiz biz;

    @RequestMapping(value = "/riskverify", method = RequestMethod.POST)
    public
    @ResponseBody
    RiskVerifyResponse riskverify(@RequestBody RiskVerifyRequest req) {
        RiskVerifyResponse resp = new RiskVerifyResponse();

        //send to mq

        //send rest
        ResponseEntity<Map> resp0 = biz.restSender(req);
        String msg = Utils.JSON.toJSONString(resp0.getBody());
        biz.sendToMQ(msg);

        return resp;
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkHealth() {
        return "hello!";
    }

}
