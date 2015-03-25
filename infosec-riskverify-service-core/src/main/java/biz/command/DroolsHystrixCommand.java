package biz.command;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.counters.CounterRepository;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.netflix.hystrix.*;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by zhangsx on 2015/1/6.
 */
public class DroolsHystrixCommand extends HystrixCommand<RiskFact> {
    private static final org.slf4j.Logger loggerDebug = LoggerFactory.getLogger(DroolsHystrixCommand.class);
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("biz");
    private static String url = GlobalConfig.getString("RuleEngineUrl");
    private RiskFact req;

    private static final int coreSize = GlobalConfig.getInteger("hystrix.ruleengine.coreSize", 64);
    private static final int maxQueueSize = GlobalConfig.getInteger("hystrix.ruleengine.maxQueueSize", 20);
    private static final int timeout = GlobalConfig.getInteger("hystrix.ruleengine.timeout", 2000);

    public DroolsHystrixCommand(RiskFact req) {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RuleEngineGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("RuleEngineCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(timeout))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize).withQueueSizeRejectionThreshold(maxQueueSize)));
        this.req = req;
    }

    @Override
    protected RiskFact run() throws Exception {
        String fact = Utils.JSON.toJSONString(req);
        return Utils.JSON.parseObject(Request.Post(url)
                .body(new StringEntity(fact, "UTF-8"))
                .connectTimeout(1000).socketTimeout(5000)
                .execute().returnContent().asString(), RiskFact.class);
    }

    @Override
    protected RiskFact getFallback() {
        req.setFinalResult(Configs.DEFAULT_RESULTS);
        String logPrefix = "[" + req.getEventPoint() + "][" + req.getEventId() + "] ";
        CounterRepository.increaseCounter(Channel.REST.toString(), 0, true);
        logger.info(logPrefix + "[step4]" + Utils.JSON.toJSONString(req));
        loggerDebug.error("call droolengine failed.");
        return req;
    }
}
