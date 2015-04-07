package receiverImpl;

import com.ctrip.cmessaging.client.IAsyncConsumer;
import com.ctrip.cmessaging.client.IMessage;
import com.ctrip.cmessaging.client.content.AckMode;
import com.ctrip.cmessaging.client.event.IConsumerCallbackEventHandler;
import com.ctrip.cmessaging.client.exception.IllegalExchangeName;
import com.ctrip.cmessaging.client.exception.IllegalTopic;
import com.ctrip.cmessaging.client.impl.Config;
import com.ctrip.cmessaging.client.impl.ConsumerFactory;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.counters.CounterRepository;
import com.ctrip.infosec.sars.util.GlobalConfig;
import manager.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import standardMiddlewareImpl.StandardMiddleware;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class CMessage implements Receiver {

    private volatile ReceiverStatus status;
    @Autowired
    @Qualifier(value = "orderIndexStandard")
    private StandardMiddleware standardMiddleware;
//    private final String FACT = "CMessage";
    private IAsyncConsumer consumer = null;
    private String identifier;
    private String subject;
    private String exchange;
    private String eventPoint;
    private static final Logger logger = LoggerFactory.getLogger(CMessage.class);

    public CMessage(String identifier, String subject, String exchange, String eventPoint) {
        this.identifier = identifier;
        this.subject = subject;
        this.exchange = exchange;
        this.eventPoint = eventPoint;
        status = ReceiverStatus.init;

    }

    /**
     * FWS:http://ws.config.framework.fws.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     * UAT:http://ws.config.framework.uat.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     * PRD:http://ws.config.framework.sh.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     */
    @Override
    public void start() {
        if (status == ReceiverStatus.running) {
            return;
        }

        logger.info("CMessage[" + eventPoint + "] start ...");
        try {
            Config.setConfigWsUri(GlobalConfig.getString("CMessageUrl"));
//        Config.setConfigWsUri("http://ws.config.framework.sh.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/");
            Config.setAppId("100000557");

            consumer = ConsumerFactory.instance.createConsumerAsAsync(identifier, subject, exchange);
            consumer.addConsumerCallbackEventHandler(new IConsumerCallbackEventHandler() {
                @Override
                public void callback(IMessage iMessage) throws Exception {
                    try {
                        standardMiddleware.assembleAndSend(Channel.CMessage, eventPoint, iMessage.getBody());
                    } catch (Throwable t) {
                        CounterRepository.increaseCounter(Channel.CMessage.toString(), 0, true);
                        logger.error("CMessage[" + eventPoint + "] ConsumerCallbackEventHandler error.", t);
                    } finally {
//                        iMessage.setAcks(AckMode.Ack);
                        iMessage.dispose();
                    }
                }
            });
            consumer.setBatchSize(20);
            consumer.ConsumeAsync(5, true);

            status = ReceiverStatus.running;
            logger.info("CMessage[" + eventPoint + "] start ... OK");
        } catch (IllegalTopic illegalTopic) {
            logger.error("CMessage[" + eventPoint + "] call start failed.", illegalTopic);
            throw new RuntimeException(illegalTopic);
        } catch (IllegalExchangeName illegalExchangeName) {
            logger.error("CMessage[" + eventPoint + "] call start failed.", illegalExchangeName);
            throw new RuntimeException(illegalExchangeName);
        }

    }

    @Override
    public void stop() {
        logger.info("CMessage[" + eventPoint + "] stop ...");
        if (consumer != null && status == ReceiverStatus.running) {
            consumer.stop();
            status = ReceiverStatus.stoped;
            logger.info("CMessage[" + eventPoint + "] stop ... OK");
        }
    }

    @Override
    public void restart() {
        logger.info("CMessage[" + eventPoint + "] restart ...");
        stop();
        start();
        logger.info("CMessage[" + eventPoint + "] restart ... OK");
    }

}
