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
import enums.InnerEnum;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
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
        RiskFact fact = (RiskFact) map.get(InnerEnum.BODY.toString());
        String channel = map.get(InnerEnum.FACT.toString()).toString();

        // 事件预处理
        long receiveTime = new Date().getTime();
        fact.setRequestReceive(sdf.format(receiveTime));
        fact.setEventId(Configs.timeBasedUUID());
        Configs.normalizeEvent(fact);

        if (fact.getExt() == null) {
            fact.setExt(new HashMap<String, Object>());
        }
        fact.getExt().put(Ext.CHANNEL, channel);
        fact.getExt().put("descTimestamp", (4070880000000L - receiveTime));

        String logPrefix = "[" + channel + "][" + fact.getEventPoint() + "][" + fact.getEventId() + "]";
        logger.info(logPrefix + "[step0]" + Utils.JSON.toJSONString(fact));

        // 验证EventPoint
        if (!Configs.isValidEventPoint(fact.getEventPoint())) {
            RiskResult result = new RiskResult();
            result.setEventId(fact.getEventId());
            result.setEventPoint(fact.getEventPoint());
            result.setRequestTime(fact.getRequestTime());
            result.setRequestReceive(fact.getRequestReceive());
            result.setResponseReceive(sdf.format(new Date()));
            result.setResponseTime(sdf.format(new Date()));
            result.setResults(invalidEventPointResult);
            logger.info(logPrefix + "[step1]" + Utils.JSON.toJSONString(result));
            return result;
        }

        // 
        String factTxt = null;
        RiskResult result = null;
        if (!Configs.hasSyncRules(fact)) {
            result = new RiskResult();
            result.setEventId(fact.getEventId());
            result.setEventPoint(fact.getEventPoint());
            result.setRequestTime(fact.getRequestTime());
            result.setRequestReceive(fact.getRequestReceive());
            result.setResponseTime(sdf.format(new Date()));
            result.setResults(Configs.DEFAULT_RESULTS);

            factTxt = Utils.JSON.toJSONString(fact);
            logger.info(logPrefix + "[step2]" + Utils.JSON.toJSONString(result));
        } else {
            // 执行同步规则
            DroolsHystrixCommand drools_command = new DroolsHystrixCommand(fact);
            fact = drools_command.execute();
            if (fact.getExt() == null) {
                fact.setExt(new HashMap<String, Object>());
            }
            result = transform(fact);

            factTxt = Utils.JSON.toJSONString(fact);
            logger.info(logPrefix + "[step3]" + factTxt);
        }

        // 发往异步规则
        logger.info(logPrefix + "send to aysnc rule engine ...");
        sender.convertAndSend(exchangeName, routingKey, factTxt);
        logger.info(logPrefix + "send to aysnc rule engine ... OK");
        return result;
    }

    private RiskResult transform(RiskFact req) {
        RiskResult result = new RiskResult();
        result.setRequestReceive(req.getRequestReceive());
        result.setEventPoint(req.getEventPoint());
        result.setEventId(req.getEventId());
        result.setRequestTime(req.getRequestTime());
        result.setResponseReceive(sdf.format(new Date()));
        result.setResponseTime(sdf.format(new Date()));
        result.setResults(req.getFinalResult());
        return result;
    }
}
