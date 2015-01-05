package com.ctrip.infosec.riskverify.rest;

import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.entity.RiskVerifyEventData;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    RiskVerifyEventData riskverify(@RequestBody RiskVerifyEventData req) {
       if (Configs.isValidEventPoint(req.getEventPoint()) == true) {
            req.setEventId(Configs.timeBasedUUID());
            req.setEvent(Configs.normalizeEvent(req.getEventPoint(),req.getEvent()));
        }
        //send rest
        ResponseEntity<Map> resp0 = biz.restSender(req);
        String msg = Utils.JSON.toJSONString(resp0.getBody());
        biz.sendToMQ(msg);

        return req;
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkHealth() {
        return "hello!";
    }

}
