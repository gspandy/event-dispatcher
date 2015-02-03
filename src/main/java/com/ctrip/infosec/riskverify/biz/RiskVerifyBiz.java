package com.ctrip.infosec.riskverify.biz;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.riskverify.biz.command.DroolsHystrixCommand;
import com.ctrip.infosec.riskverify.biz.command.RabbitMqHystrixCommand;
import com.ctrip.infosec.riskverify.biz.exception.ValidFailedException;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.collect.ImmutableMap;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RiskVerifyBiz {
    private static final Logger logger = LoggerFactory.getLogger(RiskVerifyBiz.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public RiskResult exe(RiskFact req,final String fact) {
        req.setRequestReceive(sdf.format(new Date()));
        req.setEventId(Configs.timeBasedUUID());
        Configs.normalizeEvent(req);
        if(req.getExt()==null) {
            req.setExt(new HashMap<String, Object>());
        }
        req.getExt().put("CHANNEL", fact);
        RiskResult resp = new RiskResult();

        if (!Configs.isValidEventPoint(req.getEventPoint())) {
//            //TODO set 403 code and retrun response
            resp.setEventId(req.getEventId());
            resp.setEventPoint(req.getEventPoint());
            resp.setRequestTime(req.getRequestTime());
            resp.setResponseReceive(sdf.format(new Date()));
            resp.setResponseTime(sdf.format(new Date()));
            resp.setResults(ImmutableMap.<String, Object>of("riskLevel", Integer.valueOf(0), "riskMessage", "非法的EventPoint"));
            return resp;
        }
        if(Configs.hasSyncRules(req)){
            DroolsHystrixCommand drools_command = new DroolsHystrixCommand(req);
            resp =  drools_command.execute();
            logger.info(Utils.JSON.toJSONString(resp));
            if(req.getExt()==null){
                req.setExt(new HashMap<String, Object>());
            }
            req.getExt().put("SYNC_RULE_EXECUTED", true);
        }

        RabbitMqHystrixCommand mq_command = new RabbitMqHystrixCommand(req);
        mq_command.execute();

        logger.info(Utils.JSON.toJSONString(req));
//        System.out.println(Utils.JSON.toJSONString(req));
        return resp;
    }

}

