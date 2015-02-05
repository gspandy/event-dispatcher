package com.ctrip.infosec.riskverify.biz.rabbitmq;

import com.ctrip.infosec.riskverify.Lifecycle;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by zhangsx on 2014/12/26.
 */
public class RabbitMqSender implements Lifecycle{
    private final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel channel = null;

    public void send(byte[] msg){
        try {
            channel.basicPublish("infosec.ruleengine.exchange", "ruleengine", null, msg);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void send(String msg){
        send(msg.getBytes(Charset.forName("utf-8")));
    }

    @Override
    public void init() {
        factory = new ConnectionFactory();
        factory.setHost(GlobalConfig.getString("InnerHost"));
        factory.setVirtualHost(GlobalConfig.getString("InnerVirtualHost"));
        factory.setUsername(GlobalConfig.getString("InnerUsername"));
        factory.setPassword(GlobalConfig.getString("InnerPassword"));
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException");
        }
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

    @Override
    public void recovery() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
