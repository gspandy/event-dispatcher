package com.ctrip.infosec.riskverify.biz.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;
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
    private static volatile boolean stop = false;

    private static LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();

    public static RabbitMqSender getInstance() throws Exception {
        if (sender == null) {
            try {
                lockInstance.lock();
                if (sender == null) {
                    sender = new RabbitMqSender();
                }
            } catch (Exception t) {
                logger.warn("create instance of RabbitMqSender exception");
                stop = true;
                throw t;
            } finally {
                lockInstance.unlock();
            }
        }
        return sender;
    }

    private RabbitMqSender() throws Exception {
        createConn();
        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!stop) {
                        byte[] temp = null;
                        try {
                            temp = queue.take();
                        } catch (InterruptedException e) {
                            logger.warn(e.toString());
                        }
                        if (temp != null) {
                            try {
                                channel.basicPublish("infosec.ruleengine.exchange", "ruleengine", null, temp);
                            } catch (IOException e) {
                                //TODO reconnect
                                createConn();
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    private static void createConn() {
        factory = new ConnectionFactory();
        factory.setHost("10.3.6.42");
        factory.setVirtualHost("medusa");
        factory.setUsername("bsc-medusa");
        factory.setPassword("bsc-medusa");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            stop = false;
        } catch (IOException ex) {
            if(!stop){
                stop = true;
                final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                service.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        createConn();
                        if (!stop) {
                            service.shutdownNow();
                        }
                    }
                }, 0L, 60L, TimeUnit.SECONDS);
            }

        }
    }

    public void send(byte[] msg) throws Exception {
        if (!stop && msg != null) {
            queue.put(msg);
        }
    }

    public void send(String msg) throws Exception {
        send(msg.getBytes());
        logger.info("mq send msg:" + msg);
    }

}
