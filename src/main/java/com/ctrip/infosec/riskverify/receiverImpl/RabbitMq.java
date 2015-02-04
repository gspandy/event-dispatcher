package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.riskverify.Handler;
import com.ctrip.infosec.riskverify.Receiver;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class RabbitMq implements Receiver {
    private final String FACT = "RabbitMq";
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel channel = null;
    private QueueingConsumer consumer = null;
    private volatile boolean stop = false;
    @Autowired
    private Handler handler;

    @Override
    public void init() {
        throw new RuntimeException("过期方法");
    }

    @Override
    public void start() {
        factory = new ConnectionFactory();
        factory.setHost(GlobalConfig.getString("EventHost"));
        factory.setVirtualHost(GlobalConfig.getString("EventVirtualHost"));
        factory.setUsername(GlobalConfig.getString("EventUsername"));
        factory.setPassword(GlobalConfig.getString("EventPassword"));

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException");
        }
        consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume("infosec.eventdispatcher.queue", true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException");
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    QueueingConsumer.Delivery delivery = null;
                    try {
                        delivery = consumer.nextDelivery();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException("InterruptedException");
                    }
                    //TODO
                    RiskFact fact = Utils.JSON.parseObject(new String(delivery.getBody(), Charset.forName("utf-8")), RiskFact.class);
                    if (fact != null) {
                        handler.send(ImmutableMap.of("FACT", fact, "CP", fact.getEventPoint(), "body", fact));
                    }
                }
            }
        });

    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public void recovery() {

    }
}
