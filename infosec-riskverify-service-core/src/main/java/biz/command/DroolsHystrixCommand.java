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
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by zhangsx on 2015/1/6.
 */
public class DroolsHystrixCommand extends HystrixCommand<RiskResult> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DroolsHystrixCommand.class);
    private FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
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
    protected RiskResult run() throws Exception {
        String fact = Utils.JSON.toJSONString(req);
        byte[] response = Request.Post(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Encoding", "utf-8")
                .bodyString(fact, ContentType.APPLICATION_JSON)
                .connectTimeout(5000)
                .socketTimeout(10000)
                .execute().returnContent().asBytes();
        RiskFact riskFact = Utils.JSON.parseObject(new String(response, "utf-8"), RiskFact.class);
        RiskResult result = transform(riskFact, true);

        return result;
    }

    @Override
    protected RiskResult getFallback() {
        String logPrefix = "[" + req.getEventPoint() + "][" + req.getEventId() + "] ";
        CounterRepository.increaseCounter(Channel.REST.toString(), 0, true);
        logger.error(logPrefix + "invoke ruleEngine timeout or exception.");
        return transform(req, false);
    }

    private RiskResult transform(RiskFact req, boolean isSuccess) {
        RiskResult result = new RiskResult();
        result.setRequestReceive(req.getRequestReceive());
        result.setEventPoint(req.getEventPoint());
        result.setEventId(req.getEventId());
        result.setRequestTime(req.getRequestTime());
        result.setResponseReceive(sdf.format(new Date()));
        result.setResponseTime(sdf.format(new Date()));
        result.setResults(req.getFinalResult());
        if(!isSuccess||req.getFinalResult()==null){
            result.setResults(Configs.DEFAULT_RESULTS);
        }
        return result;
    }
}
