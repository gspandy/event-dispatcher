package com.ctrip.infosec.riskverify.rest;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.exception.ValidFailedException;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangsx on 2014/12/22.
 */
@Controller
public class RiskRestImpl {
    @Autowired
    private RiskVerifyBiz biz;
    private final String fact = "REST";
    @RequestMapping(value = "/riskverify", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<RiskFact> riskverify(@RequestBody RiskFact req) {
        try {
            return new ResponseEntity<RiskFact>(biz.exe(req,fact), HttpStatus.OK);
        } catch (ValidFailedException e) {
            return new ResponseEntity<RiskFact>(req, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkHealth() {
        return "hello!";
    }

}
