package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.infosec.riskverify.Receiver;
import com.ctrip.infosec.riskverify.StandardMiddleware;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangsx on 2015/2/4.
 */
public class SecLog implements Receiver {
    private final String FACT = "SecLog";
    //    private String cp;
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel channel = null;
    private QueueingConsumer consumer = null;
    private volatile boolean stop = false;
    private static final Logger logger = LoggerFactory.getLogger(SecLog.class);
    private volatile ReceiverStatus status;

    @Autowired
    @Qualifier(value = "secStandard")
    private StandardMiddleware standardMiddleware;

//    public SecLog(String cp) {
//        this.cp = cp;
//    }

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

        factory = new ConnectionFactory();
        factory.setHost(GlobalConfig.getString("SecLogHost"));
        factory.setVirtualHost(GlobalConfig.getString("SecLogVirtualHost"));
        factory.setUsername(GlobalConfig.getString("SecLogUsername"));
        factory.setPassword(GlobalConfig.getString("SecLogPassword"));

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("IOException");
        }
        consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume("risk-log", true, consumer);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("IOException");
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (status == ReceiverStatus.running) {
                        QueueingConsumer.Delivery delivery = null;
                        try {
                            delivery = consumer.nextDelivery();
                            standardMiddleware.assembleAndSend(ImmutableMap.of("fact", FACT, "body", delivery.getBody()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException("InterruptedException");
                        }
                    }
                    executorService.shutdown();
                }
            });
        }

    }

    @Override
    public void stop() {
        if (status == ReceiverStatus.running) {
            status = ReceiverStatus.stoped;
            logger.info("seclog stoped");
        }
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
