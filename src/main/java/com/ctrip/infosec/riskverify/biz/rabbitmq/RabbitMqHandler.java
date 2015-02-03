package com.ctrip.infosec.riskverify.biz.rabbitmq;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.Charset;
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

    private static volatile boolean stop = false;

    @Autowired
    private RiskVerifyBiz biz;
    public RabbitMqHandler () throws Throwable{
        createConn();
    }

    private static void createConn() throws Throwable{

        try {
            lock.lock();

            factory = new ConnectionFactory();
            factory.setHost(GlobalConfig.getString("EventHost"));
            factory.setVirtualHost(GlobalConfig.getString("EventVirtualHost"));
            factory.setUsername(GlobalConfig.getString("EventUsername"));
            factory.setPassword(GlobalConfig.getString("EventPassword"));


            connection = factory.newConnection();
            channel = connection.createChannel();
            consumer = new QueueingConsumer(channel);
            channel.basicConsume("infosec.eventdispatcher.queue", true, consumer);
            stop = false;

        } catch (IOException ex){
            stop = true;
            throw ex;
        } catch (Throwable t) {

            logger.warn("RabbitMqHandler构造函数异常:" + t.getMessage());
        } finally {
            lock.unlock();
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
//                            logger.info("handle eventqueue msg:" + new String(delivery.getBody()));
                            //TODO
                            RiskFact fact = Utils.JSON.parseObject(new String(delivery.getBody(), Charset.forName("utf-8")), RiskFact.class);
                            if(fact!=null){
                                biz.exe(fact,"MQ");
                            }
                        }else{
                            Thread.sleep(1000);
                        }
                    } catch (ConsumerCancelledException t) {
                        stop = true;
                        final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                        service.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    createConn();
                                } catch (Throwable throwable) {

                                }
                                if (!stop) {
                                    service.shutdownNow();
                                }
                            }
                        }, 0L, 60L, TimeUnit.SECONDS);
                    }catch (Throwable t){
                        logger.warn(t.getMessage());
                    }
                }
            }
        });

    }
}
