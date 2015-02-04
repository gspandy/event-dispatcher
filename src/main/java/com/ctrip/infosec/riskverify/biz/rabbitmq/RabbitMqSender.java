package com.ctrip.infosec.riskverify.biz.rabbitmq;

import com.ctrip.infosec.sars.util.GlobalConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangsx on 2014/12/26.
 */
public class RabbitMqSender {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);
    private static ConnectionFactory factory = null;
    private static Connection connection = null;
    private static Channel channel = null;
    private static Lock lockInstance = new ReentrantLock();
    private static RabbitMqSender sender = null;

    public static RabbitMqSender getInstance() {

        if (sender == null) {
            try {
                lockInstance.lock();
                if (sender == null) {
                    sender = new RabbitMqSender();
                    createConn();
                }
            } finally {
                lockInstance.unlock();
            }
        }
        return sender;
    }

    private RabbitMqSender(){

    }

    private static void createConn() {
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

    public void send(byte[] msg) throws Exception {
        channel.basicPublish("infosec.ruleengine.exchange", "ruleengine", null, msg);
    }

    public void send(String msg) throws Exception {
        send(msg.getBytes(Charset.forName("utf-8")));
    }

}
