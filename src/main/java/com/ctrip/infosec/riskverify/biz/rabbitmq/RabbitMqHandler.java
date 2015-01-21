package com.ctrip.infosec.riskverify.biz.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangsx on 2015/1/21.
 */
public class RabbitMqHandler {
    private static ConnectionFactory factory = null;
    private static Connection connection = null;
    private static Channel channel = null;
    private static Lock lock = new ReentrantLock();
    private static QueueingConsumer consumer = null;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqHandler.class);

    private static volatile boolean stop = true;

    public RabbitMqHandler() {
        createConn();
    }

    private static void createConn(){
        if (factory == null || connection == null || channel == null || consumer == null) {
            try {
                lock.lock();
                if (factory == null || connection == null || channel == null) {
                    factory = new ConnectionFactory();
                    factory.setHost("10.3.6.42");
                    factory.setVirtualHost("medusa");
                    factory.setUsername("bsc-medusa");
                    factory.setPassword("bsc-medusa");

                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    consumer = new QueueingConsumer(channel);
                    channel.basicConsume("infosec.eventdispatcher.queue", true, consumer);

                    stop=false;
                }
            } catch (Throwable t) {
                stop=true;
                logger.warn("RabbitMqHandler构造函数异常:" + t.getMessage());
            } finally {
                lock.unlock();
            }


        }
    }

    /**
     * 连接异常每60s自连接
     */
    public void init() {

        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!stop) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                            logger.info("handle eventqueue msg:" + new String(delivery.getBody()));
                        }
                    } catch (Throwable t) {
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
        });

    }
}
