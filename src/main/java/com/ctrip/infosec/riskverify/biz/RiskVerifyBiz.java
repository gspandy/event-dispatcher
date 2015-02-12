package com.ctrip.infosec.riskverify.biz;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.Ext;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.configs.event.monitor.EventCounterRepository;
import com.ctrip.infosec.riskverify.biz.command.DroolsHystrixCommand;
import com.ctrip.infosec.riskverify.biz.rabbitmq.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RiskVerifyBiz {

    private static final Logger logger = LoggerFactory.getLogger(RiskVerifyBiz.class);
    @Autowired
    private RabbitMqSender sender;
    private FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
    /**
     * 非法的EventPoint
     */
    private final Map<String, Object> invalidEventPointResult = ImmutableMap.<String, Object>of("riskLevel", Integer.valueOf(0), "riskMessage", "非法的EventPoint");

    public RiskResult exe(Map map) {
        RiskFact fact = (RiskFact) map.get("body");
//        String cp = map.get("CP").toString();
        String channel = map.get("FACT").toString();

        // 设置时间戳及扩展字段
        fact.setEventId(Configs.timeBasedUUID());
        fact.setRequestReceive(fastDateFormat.format(new Date()));

        String logPrefix = "[" + channel + "][" + fact.getEventPoint() + "][" + fact.getEventId() + "] ";
        logger.info(logPrefix + "event: " + Utils.JSON.toJSONString(fact.getEventBody()));
        if (fact.getExt() == null) {
            fact.setExt(new HashMap<String, Object>());
        }
        fact.getExt().put(Ext.CHANNEL, channel);
        // 数据标准化
        Configs.normalizeEvent(fact);

        RiskResult result = new RiskResult();
        if (!Configs.isValidEventPoint(fact.getEventPoint())) {
            result.setEventId(fact.getEventId());
            result.setEventPoint(fact.getEventPoint());
            result.setRequestTime(fact.getRequestTime());
            result.setRequestReceive(fact.getRequestReceive());
            result.setResponseReceive(fastDateFormat.format(new Date()));
            result.setResponseTime(fastDateFormat.format(new Date()));
            result.setResults(invalidEventPointResult);
            logger.info(logPrefix + "result: " + Utils.JSON.toJSONString(result));
            return result;
        }

        // 执行同步规则
        if (Configs.hasSyncRules(fact)) {
            DroolsHystrixCommand drools_command = new DroolsHystrixCommand(fact);
            result = drools_command.execute();
            if (fact.getExt() == null) {
                fact.setExt(new HashMap<String, Object>());
            }
            fact.getExt().put("SYNC_RULE_EXECUTED", true);
        }

        // 执行异步规则
        String s = Utils.JSON.toJSONString(fact);
        sender.send(s);

        // 流量统计
        Channel ch = Channel.parse(channel);
        ch = (ch == null) ? Channel.MQ : ch;
        EventCounterRepository.increaseCounter(fact.eventPoint, ch);

        logger.info(logPrefix + "result: " + Utils.JSON.toJSONString(result));
        return result;
    }

}
