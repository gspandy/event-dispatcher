package com.ctrip.infosec.riskverify.biz.command;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsx on 2015/1/21.
 */
@Deprecated
public class RabbitMqHystrixCommand extends HystrixCommand {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RabbitMqHystrixCommand.class);
    private RiskFact req;
    public RabbitMqHystrixCommand(RiskFact req) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("rabbitmq"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationThreadTimeoutInMilliseconds(2000)));
        this.req = req;
    }

    @Override
    protected Object run() throws Exception {
        throw new Exception();
    }
    @Override
    protected Object getFallback() {
        logger.error("call error", this.getFailedExecutionException());
        return null;
    }
}