package com.ctrip.infosec.riskverify.command;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.rabbitmq.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsx on 2015/1/6.
 */
public class DroolsHystrixCommand extends HystrixCommand<RiskFact> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DroolsHystrixCommand.class);
    private RiskFact req;

    public DroolsHystrixCommand(RiskFact req) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("drools"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationThreadTimeoutInMilliseconds(2000)));
        this.req = req;
    }

    @Override
    protected RiskFact run() throws Exception {
        String fact = Utils.JSON.toJSONString(req);
        byte[] response = Request.Post("http://10.3.6.104:8090/rule/query")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Encoding", "utf-8")
                .bodyString(fact, ContentType.APPLICATION_JSON)
                .connectTimeout(5000)
                .socketTimeout(10000)
                .execute().returnContent().asBytes();

        RiskFact riskFact = Utils.JSON.parseObject(new String(response,"utf-8"),RiskFact.class);
        logger.info("success");
        return riskFact;
    }

    @Override
    protected RiskFact getFallback() {
        logger.info("call drools rest fail and req EventId=" + req.getEventId());
        return req;
    }
}
