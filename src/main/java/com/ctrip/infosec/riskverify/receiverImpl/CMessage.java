package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.cmessaging.client.IAsyncConsumer;
import com.ctrip.cmessaging.client.IMessage;
import com.ctrip.cmessaging.client.content.AckMode;
import com.ctrip.cmessaging.client.event.IConsumerCallbackEventHandler;
import com.ctrip.cmessaging.client.exception.IllegalExchangeName;
import com.ctrip.cmessaging.client.exception.IllegalTopic;
import com.ctrip.cmessaging.client.impl.Config;
import com.ctrip.cmessaging.client.impl.ConsumerFactory;
import com.ctrip.infosec.riskverify.Receiver;
import com.ctrip.infosec.riskverify.StandardMiddleware;
import com.ctrip.infosec.sars.util.GlobalConfig;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class CMessage implements Receiver {
    private volatile ReceiverStatus status;
    @Autowired
    private StandardMiddleware standardMiddleware;
    private final String FACT = "CMessage";
    private IAsyncConsumer consumer = null;
    private String identifier;
    private String subject;
    private String exchange;
    private String cp;
    private static final Logger logger = LoggerFactory.getLogger(CMessage.class);
    private LinkedBlockingQueue<Map> queue = new LinkedBlockingQueue<Map>();

    public CMessage(String identifier,String subject,String exchange,String cp){
        this.identifier = identifier;
        this.subject = subject;
        this.exchange = exchange;
        this.cp = cp;
        status = ReceiverStatus.init;

    }
    @Override
    public void init() {
        throw new RuntimeException("过期方法");
    }

    /**
     * FWS:http://ws.config.framework.fws.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     * UAT:http://ws.config.framework.uat.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     * PRD:http://ws.config.framework.sh.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     */
    @Override
    public void start() {
        if (status==ReceiverStatus.running){
            return;
        }
        status = ReceiverStatus.running;

//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                while(status==ReceiverStatus.running){
//                    try {
//                        standardMiddleware.assembleAndSend(queue.take());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        throw new RuntimeException("InterruptedException");
//                    }
//                }
//            }
//        });

        logger.info("cmessage start");
        try {
            consumer = ConsumerFactory.instance.createConsumerAsAsync(identifier, subject, exchange);
        } catch (IllegalTopic illegalTopic) {
            //TODO
            throw new RuntimeException(illegalTopic);
        } catch (IllegalExchangeName illegalExchangeName) {
            //TODO
            throw new RuntimeException(illegalExchangeName);
        }
//        Config.setConfigWsUri(GlobalConfig.getString("CMessageUrl"));
        Config.setConfigWsUri("http://ws.config.framework.sh.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/");
        Config.setAppId("100000557");
        if (consumer != null) {
            consumer.addConsumerCallbackEventHandler(new IConsumerCallbackEventHandler() {
                @Override
                public void callback(IMessage iMessage) throws Exception {
                    try {
                        standardMiddleware.assembleAndSend(ImmutableMap.of("fact", FACT, "CP", cp, "body", iMessage.getBody()));
//                        queue.offer(ImmutableMap.of("fact", FACT, "CP", cp, "body", iMessage.getBody()));
                    }catch (Throwable t){
                        iMessage.setAcks(AckMode.Nack);
                        logger.error(t.toString());
                        throw new RuntimeException(t);
                    }finally {
                        iMessage.setAcks(AckMode.Ack);
                        iMessage.dispose();
                    }

                }
            });
            consumer.setBatchSize(20);
            consumer.ConsumeAsync(5, false);
        }
    }

    @Override
    public void stop() {
        if(consumer!=null&&status==ReceiverStatus.running){
            consumer.stop();
            status = ReceiverStatus.stoped;
            logger.info("cmessage stop");
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
