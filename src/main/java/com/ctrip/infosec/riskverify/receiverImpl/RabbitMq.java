package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.Handler;
import com.ctrip.infosec.riskverify.Receiver;
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
 * Created by zhangsx on 2015/2/3.
 */
public class RabbitMq implements Receiver ,MessageListener{
    private final String FACT = "RabbitMq";
    private static final Logger logger = LoggerFactory.getLogger(RabbitMq.class);
    private SimpleMessageListenerContainer container;
    @Autowired
    private Handler handler;

    @Autowired
    @Qualifier(value = "connectionFactory1")
    private ConnectionFactory factory;

    @Override
    public void init() {
        throw new RuntimeException("过期方法");
    }

    @Override
    public void start() {
        container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addQueues(new Queue("risk-log"));
        container.setMessageListener(this);
        container.setAutoDeclare(true);
        container.setMaxConcurrentConsumers(Runtime.getRuntime().availableProcessors());

        container.start();
    }

    @Override
    public void stop() {
        container.shutdown();
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public void onMessage(Message message) {
        if(message!=null){
            RiskFact fact = Utils.JSON.parseObject(new String(message.getBody(), Charset.forName("utf-8")), RiskFact.class);
            handler.send(ImmutableMap.of("FACT", fact, "CP", fact.getEventPoint(), "body", fact));
        }
    }
}
