package biz.command;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.Ext;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.counters.CounterRepository;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.netflix.hystrix.*;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.slf4j.LoggerFactory;


/**
 * Created by zhangsx on 2015/1/6.
 */
@Deprecated
public class DroolsHystrixCommand extends HystrixCommand<RiskFact> {

    private static final org.slf4j.Logger loggerDebug = LoggerFactory.getLogger(DroolsHystrixCommand.class);
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("biz");
    private static String url = GlobalConfig.getString("RuleEngineUrl");
    private RiskFact fact;

    private static final int coreSize = GlobalConfig.getInteger("hystrix.ruleengine.coreSize", 64);
    private static final int maxQueueSize = GlobalConfig.getInteger("hystrix.ruleengine.maxQueueSize", 20);
    private static final int timeout = GlobalConfig.getInteger("hystrix.ruleengine.timeout", 2000);

    public DroolsHystrixCommand(RiskFact fact) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RuleEngineGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("RuleEngineCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(timeout))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize).withQueueSizeRejectionThreshold(maxQueueSize)));
        this.fact = fact;
    }

    @Override
    protected RiskFact run() throws Exception {
        String factTxt = Utils.JSON.toJSONString(fact);

        fact = Utils.JSON.parseObject(Request.Post(url)
                .body(new StringEntity(factTxt, "UTF-8"))
                .connectTimeout(1000).socketTimeout(2000)
                .execute().returnContent().asString(), RiskFact.class);

        // 设置同步已执行的标识
        fact.getExt().put(Ext.SYNC_RULE_EXECUTED, true);
        return fact;
    }

    @Override
    protected RiskFact getFallback() {
        fact.setFinalResult(Configs.DEFAULT_RESULTS);
        String logPrefix = "[" + fact.getEventPoint() + "][" + fact.getEventId() + "] ";
        CounterRepository.increaseCounter(Channel.REST.toString(), 0, true);
        logger.info(logPrefix + "[step4]" + Utils.JSON.toJSONString(fact));
        loggerDebug.error("call droolengine failed.");
        return fact;
    }
}
