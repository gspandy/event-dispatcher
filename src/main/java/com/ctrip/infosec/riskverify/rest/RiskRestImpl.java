package com.ctrip.infosec.riskverify.rest;

import com.ctrip.infosec.common.model.RiskFact;
import static com.ctrip.infosec.configs.utils.Utils.JSON;

import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.biz.exception.ValidFailedException;
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
 * Created by zhangsx on 2014/12/22.
 */
@Controller
public class RiskRestImpl {
    @Autowired
    private RiskVerifyBiz biz;
    private final Logger logger = LoggerFactory.getLogger(RiskRestImpl.class);
    private final String FACT = "REST";

    @RequestMapping(value = "/riskverify", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<RiskResult> riskverify(@RequestBody RiskFact req) {
        logger.info("REST: " + JSON.toJSONString(req));
        RiskResult respBody = null;
        try {
            respBody = biz.exe(req, FACT);
        } catch (ValidFailedException e) {
            logger.warn("http 403 exception");
        } catch (Exception ex){
            logger.warn(ex.toString());
        }catch (Throwable t){
            logger.error(t.toString());
        }
        return new ResponseEntity<RiskResult>(respBody, HttpStatus.OK);
    }


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkHealth() {
        return "hello!";
    }

}
