package com.ctrip.infosec.riskverify.biz.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

    private static Lock lock = new ReentrantLock();


    public RabbitMqSender() {
        if (factory == null || connection == null || channel == null) {
            try{
                lock.lock();
                if (factory == null || connection == null || channel == null) {
                    factory = new ConnectionFactory();
//                    Properties globalConfig = new Properties();
//                    globalConfig.load(RabbitMqSender.class.getClassLoader().getResourceAsStream("GlobalConfig.properties"));
//                    factory.setHost(globalConfig.getProperty("host"));
//                    factory.setVirtualHost(globalConfig.getProperty("virtual-host"));
//                    factory.setUsername(globalConfig.getProperty("username"));
//                    factory.setPassword(globalConfig.getProperty("password"));
                    factory.setHost("10.3.6.42");
                    factory.setVirtualHost("medusa");
                    factory.setUsername("bsc-medusa");
                    factory.setPassword("bsc-medusa");
                    connection = factory.newConnection();
                    channel = connection.createChannel();

                }
            }catch (Throwable t){

            }finally {
                lock.unlock();
            }


        }

    }

    public void send(byte[] msg) throws Exception {
        channel.basicPublish("infosec.ruleengine.exchange", "ruleengine", null, msg);

    }

    public void send(String msg) throws Exception {
        send(msg.getBytes());
        logger.info("mq send msg:"+msg);
    }

    public static void close() {
        try {
            lock.lock();
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Throwable t) {

        } finally {
            lock.unlock();
        }


    }
}
