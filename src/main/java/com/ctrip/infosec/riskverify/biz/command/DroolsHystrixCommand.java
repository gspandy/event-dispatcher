package com.ctrip.infosec.riskverify.biz.command;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangsx on 2015/1/6.
 */
public class DroolsHystrixCommand extends HystrixCommand<RiskResult> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DroolsHystrixCommand.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private RiskFact req;

    public DroolsHystrixCommand(RiskFact req) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("drools"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationThreadTimeoutInMilliseconds(2000)));
        this.req = req;
    }

    @Override
    protected RiskResult run() throws Exception {
        String fact = Utils.JSON.toJSONString(req);
        byte[] response = Request.Post("http://10.3.6.104:8090/rule/query")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Encoding", "utf-8")
                .bodyString(fact, ContentType.APPLICATION_JSON)
                .connectTimeout(5000)
                .socketTimeout(10000)
                .execute().returnContent().asBytes();

        RiskFact riskFact = Utils.JSON.parseObject(new String(response, "utf-8"), RiskFact.class);
        RiskResult result = transform(riskFact, true);

        logger.info("success");
        return result;
    }

    @Override
    protected RiskResult getFallback() {
        logger.info("call drools rest fail and req EventId=" + req.getEventId());
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
        if(!isSuccess){
            result.setResults(Configs.DEFAULT_RESULTS);
        }
        return result;
    }
}
