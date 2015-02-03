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
    private static LinkedBlockingQueue<byte[]>[] arr = new LinkedBlockingQueue[5];

    private static volatile boolean stop = false;

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

        arr[0] = new LinkedBlockingQueue<byte[]>();
        arr[1] = new LinkedBlockingQueue<byte[]>();
        arr[2] = new LinkedBlockingQueue<byte[]>();
        arr[3] = new LinkedBlockingQueue<byte[]>();
        arr[4] = new LinkedBlockingQueue<byte[]>();

        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            final int finalI = i;

            service.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            byte[] temp = arr[finalI].take();
                            if (temp != null) {
                                try {
                                    channel.basicPublish("infosec.ruleengine.exchange", "ruleengine", null, temp);
                                } catch (IOException e) {
                                    stop = true;
                                    e.printStackTrace();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private static void createConn() throws IOException {
        factory = new ConnectionFactory();
        factory.setHost(GlobalConfig.getString("InnerHost"));
        factory.setVirtualHost(GlobalConfig.getString("InnerVirtualHost"));
        factory.setUsername(GlobalConfig.getString("InnerUsername"));
        factory.setPassword(GlobalConfig.getString("InnerPassword"));

        System.out.println(GlobalConfig.getString("InnerHost"));
        System.out.println(GlobalConfig.getString("InnerVirtualHost"));
        System.out.println(GlobalConfig.getString("InnerUsername"));
        System.out.println(GlobalConfig.getString("InnerPassword"));

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void send(byte[] msg) throws Exception {
        arr[RandomUtils.nextInt() % 5].put(msg);
    }

    public void send(String msg) throws Exception {
        send(msg.getBytes(Charset.forName("utf-8")));
    }

}
