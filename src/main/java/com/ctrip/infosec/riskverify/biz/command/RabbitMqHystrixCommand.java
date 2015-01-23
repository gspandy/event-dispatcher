package com.ctrip.infosec.riskverify.biz.command;

import com.ctrip.infosec.riskverify.biz.rabbitmq.RabbitMqSender;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsx on 2015/1/21.
 */
public class RabbitMqHystrixCommand extends HystrixCommand {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RabbitMqHystrixCommand.class);
    private String msg;
    public RabbitMqHystrixCommand(String msg) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("rabbitmq"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationThreadTimeoutInMilliseconds(2000)));
        this.msg = msg;
    }

    @Override
    protected Object run() throws Exception {
        RabbitMqSender sender = RabbitMqSender.getInstance();
        sender.send(msg);
        return null;
    }
    @Override
    protected Object getFallback() {
        logger.info("call error");
        return null;
    }
}
