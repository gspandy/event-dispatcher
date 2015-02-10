package com.ctrip.infosec.riskverify;

import com.ctrip.infosec.riskverify.Lifecycle;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Deprecated
public class RabbitMqSender implements Lifecycle{
    private final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);
    private AmqpTemplate template;
    @Autowired
    @Qualifier(value = "connectionFactory1")
    private ConnectionFactory factory;

    public void send(byte[] body){
        template.send("infosec.ruleengine.exchange","ruleengine",new Message(body,new MessageProperties()));
    }

    public void send(String msg){
        send(msg.getBytes(Charset.forName("utf-8")));
    }

    @Override
    public void init() {
        template = new RabbitTemplate(factory);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }
}
