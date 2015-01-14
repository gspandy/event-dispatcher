package com.ctrip.infosec.riskverify.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RabbitMqSender {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);
    @Autowired
    private AmqpTemplate amqp;

    public void Producer(String msg) {
        logger.info("Producer");
        amqp.convertAndSend(msg);
        logger.info("Done");
    }
}
