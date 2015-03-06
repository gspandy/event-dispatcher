package biz;

import biz.command.DroolsHystrixCommand;
import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.Ext;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.counters.CounterRepository;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RiskVerifyBiz {
    private static final Logger logger = LoggerFactory.getLogger("biz");
    private FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
    private final String exchangeName = "infosec.ruleengine.exchange";
    private final String routingKey = "ruleengine";
    @Autowired
    private RabbitTemplate sender;

    /**
     * 非法的EventPoint
     */
    private final Map<String, Object> invalidEventPointResult = ImmutableMap.<String, Object>of("riskLevel", Integer.valueOf(0), "riskMessage", "非法的EventPoint");


    public RiskResult exe(Map map) {
        RiskFact req = (RiskFact)map.get("body");
        String cp = map.get("CP").toString();
        String channel = map.get("FACT").toString();

        req.setRequestReceive(sdf.format(new Date()));
        req.setEventId(Configs.timeBasedUUID());
        if(req.getExt()==null) {
            req.setExt(new HashMap<String, Object>());
        }
        req.getExt().put(Ext.CHANNEL, channel);
        Configs.normalizeEvent(req);

        String logPrefix = "[" + channel + "][" + req.getEventPoint() + "][" + req.getEventId() + "]";
        logger.info(logPrefix + "[step0]" + Utils.JSON.toJSONString(req));

        RiskResult result = new RiskResult();
        result.setEventId(req.getEventId());
        result.setEventPoint(req.getEventPoint());
        result.setRequestTime(req.getRequestTime());
        result.setRequestReceive(req.getRequestReceive());
        if (!Configs.isValidEventPoint(req.getEventPoint())) {
            result.setResponseReceive(sdf.format(new Date()));
            result.setResponseTime(sdf.format(new Date()));
            result.setResults(invalidEventPointResult);
            return result;
        }
        if(Configs.hasSyncRules(req)){
            DroolsHystrixCommand drools_command = new DroolsHystrixCommand(req);
            result =  drools_command.execute();

            if(req.getExt()==null){
                req.setExt(new HashMap<String, Object>());
            }
            req.getExt().put(Ext.SYNC_RULE_EXECUTED, true);
        }else {
            result.setResponseTime(sdf.format(new Date()));
            result.setResults(Configs.DEFAULT_RESULTS);
        }

        logger.info(logPrefix + "[step1]" + Utils.JSON.toJSONString(result));

        String s = Utils.JSON.toJSONString(req);
        sender.send(exchangeName,routingKey,new Message(s.getBytes(),new MessageProperties()));
        return result;
    }

}

