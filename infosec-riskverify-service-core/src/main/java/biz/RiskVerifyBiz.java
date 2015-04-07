package biz;

import com.ctrip.infosec.configs.utils.concurrent.PoolConfig;
import com.ctrip.infosec.configs.utils.concurrent.PooledMethodProxy;
import com.ctrip.infosec.configs.utils.concurrent.MethodProxyFactory;
import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.Ext;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.rule.venus.RuleEngineRemoteService;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.ctrip.infosec.sars.util.SpringContextHolder;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private static String url = GlobalConfig.getString("RuleEngineUrl");
    /**
     * 异步模拟同步（多线程）调用同步规则引擎.
     */
    PooledMethodProxy ruleEngineRemoteService;
    private static final int coreSize = GlobalConfig.getInteger("pooled.sync.coreSize", 32);
    private static final int maxThreadSize = GlobalConfig.getInteger("pooled.sync.maxThreadSize", 512);
    private static final int keepAliveTime = GlobalConfig.getInteger("pooled.sync.keepAliveTime", 60);
    private static final int queueSize = GlobalConfig.getInteger("pooled.sync.queueSize", -1);
    private Lock lock = new ReentrantLock();

    /**
     * 非法的EventPoint
     */
    private final Map<String, Object> invalidEventPointResult = ImmutableMap.<String, Object>of("riskLevel", Integer.valueOf(0), "riskMessage", "非法的EventPoint");

    public RiskResult exe(Channel channel, RiskFact fact) {

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

        String logPrefix = "[" + channel + "][" + fact.getEventPoint() + "][" + fact.getEventId() + "] ";
        String factTxt = Utils.JSON.toJSONString(fact);
        logger.info(logPrefix + "[step0]" + factTxt);

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

        // 执行同步规则
        if (Configs.hasSyncRules(fact)) {
            logger.info(logPrefix + "invoke sync rule engine ...");
            long timeout = Configs.timeoutInvokeSyncRule(fact.eventPoint);
            try {
//                factTxt = Request.Post(url)
//                        .body(new StringEntity(factTxt, "UTF-8"))
//                        .connectTimeout((int) timeout).socketTimeout((int) timeout)
//                        .execute().returnContent().asString();
//                fact = Utils.JSON.parseObject(factTxt, RiskFact.class);

                // 使用线程池模拟同步方式调用RuleEngine服务
                initRuleEngineRemoteService();
                fact = ruleEngineRemoteService.syncInvoke(timeout, fact);

                // 设置同步已执行的标识
                if (fact.getExt() == null) {
                    fact.setExt(new HashMap<String, Object>());
                }
                fact.getExt().put(Ext.SYNC_RULE_EXECUTED, true);
                logger.info(logPrefix + "invoke sync rule engine ... OK");

            } catch (Exception ex) {
                fact.setFinalResult(Configs.DEFAULT_RESULTS);
                logger.warn(logPrefix + "invoke sync rule engine timeout or exception.", ex);
            }
        }

        // 处理返回值
        RiskResult result = transform(fact);
        logger.info(logPrefix + "RESULT: " + Utils.JSON.toJSONString(result));

        // 发往异步规则
        logger.info(logPrefix + "send to aysnc rule engine ...");
        sender.convertAndSend(exchangeName, routingKey, Utils.JSON.toJSONString(fact));
        logger.info(logPrefix + "send to aysnc rule engine ... OK");
        return result;
    }

    private RiskResult transform(RiskFact fact) {
        RiskResult result = new RiskResult();
        result.setRequestReceive(fact.getRequestReceive());
        result.setEventPoint(fact.getEventPoint());
        result.setEventId(fact.getEventId());
        result.setRequestTime(fact.getRequestTime());
        result.setResponseReceive(sdf.format(new Date()));
        result.setResponseTime(sdf.format(new Date()));
        result.setResults(fact.getFinalResult());
        return result;
    }

    /**
     * 初始化规则引擎执行的POOL
     */
    void initRuleEngineRemoteService() {
        if (ruleEngineRemoteService == null) {
            lock.lock();
            try {
                if (ruleEngineRemoteService == null) {
                    logger.info("init rule engine client ...");
                    RuleEngineRemoteService service = SpringContextHolder.getBean(RuleEngineRemoteService.class);
                    this.ruleEngineRemoteService = MethodProxyFactory
                            .newMethodProxy(service, "verify", RiskFact.class)
                            .supportAsyncInvoke()
                            .pooledWithConfig(new PoolConfig()
                                    .withCorePoolSize(coreSize)
                                    .withKeepAliveTime(keepAliveTime)
                                    .withMaxPoolSize(maxThreadSize)
                                    .withQueueSize(queueSize)
                            );
                    logger.info("init rule engine client ... OK");
                }
            } catch (Exception e) {
                logger.info("init rule engine client ... Exception", e);
            } finally {
                lock.unlock();
            }
        }
    }
}
