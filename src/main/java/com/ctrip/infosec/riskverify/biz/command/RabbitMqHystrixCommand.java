package com.ctrip.infosec.riskverify.biz.command;

import com.ctrip.infosec.sars.util.GlobalConfig;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsx on 2015/1/21.
 */
public class RabbitMqHystrixCommand extends HystrixCommand {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqHystrixCommand.class);
    /**
     * hystrix
     */
    private static final int coreSize = GlobalConfig.getInteger("hystrix.ruleengine.coreSize", 32);
    private static final int maxQueueSize = GlobalConfig.getInteger("hystrix.ruleengine.maxQueueSize", 10);
    private static final int timeout = GlobalConfig.getInteger("hystrix.ruleengine.timeout", 2000);

    private Channel channel;
    private byte[] message;

    public RabbitMqHystrixCommand(Channel channel, byte[] message) {
        super(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RabbitMqGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("RabbitMqCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(timeout))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize).withQueueSizeRejectionThreshold(maxQueueSize)));

        this.channel = channel;
        this.message = message;
    }

    @Override
    protected Object run() throws Exception {
        channel.basicPublish("infosec.ruleengine.exchange", "ruleengine", null, message);
        return null;
    }
}
