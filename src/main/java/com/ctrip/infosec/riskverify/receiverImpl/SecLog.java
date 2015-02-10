package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.Receiver;
import com.ctrip.infosec.riskverify.StandardMiddleware;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.nio.charset.Charset;

/**
 * Created by zhangsx on 2015/2/4.
 */
public class SecLog implements Receiver{
    private final String FACT = "SecLog";
    private static final Logger logger = LoggerFactory.getLogger(SecLog.class);
    private volatile ReceiverStatus status;
    private SimpleMessageListenerContainer container;
    @Autowired
    @Qualifier(value = "secStandard")
    private StandardMiddleware standardMiddleware;

    @Autowired
    @Qualifier(value = "connectionFactory0")
    private ConnectionFactory factory;

    @Override
    public void init() {
        throw new RuntimeException("过期方法");
    }

    @Override
    public void start() {
        if (status == ReceiverStatus.running) {
            return;
        }
        status = ReceiverStatus.running;
        logger.info("seclog start");

        container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addQueues(new Queue("risk-log"));
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                standardMiddleware.assembleAndSend(ImmutableMap.of("FACT", FACT, "body",message.getBody()));
            }
        });
        container.setMaxConcurrentConsumers(Runtime.getRuntime().availableProcessors());

        container.start();
    }

    @Override
    public void stop() {
        if (status == ReceiverStatus.running) {
            status = ReceiverStatus.stoped;
            container.shutdown();
            logger.info("seclog stoped");
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

}
