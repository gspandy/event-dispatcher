package receiverImpl;

import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.counters.CounterRepository;
import manager.Lifecycle;
import manager.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import standardMiddlewareImpl.StandardMiddleware;

/**
 * Created by zhangsx on 2015/2/4.
 */
public class SecLog implements Receiver {
//    private final String FACT = "SecLog";

    private static final Logger logger = LoggerFactory.getLogger(SecLog.class);
    private volatile Lifecycle.ReceiverStatus status;
    private SimpleMessageListenerContainer container;
    @Autowired
    @Qualifier(value = "secStandard")
    private StandardMiddleware standardMiddleware;

    @Autowired
    @Qualifier(value = "connectionFactory0")
    private ConnectionFactory factory;

    @Override
    public void start() {
        if (status == ReceiverStatus.running) {
            return;
        }
        status = ReceiverStatus.running;
        logger.info("seclog start");

        container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addQueues(new Queue("risk-log"));
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    standardMiddleware.assembleAndSend(Channel.MQ, null, message.getBody());
                } catch (Throwable t) {
                    CounterRepository.increaseCounter("SecLog", 0, true);
                    logger.error("SecLog MessageListener error.", t);
                }
            }
        });
        container.setPrefetchCount(1000);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setMaxConcurrentConsumers(Runtime.getRuntime().availableProcessors() * 4);
        container.start();
    }

    @Override
    public void stop() {
        if (status == ReceiverStatus.running) {
            status = ReceiverStatus.stoped;
            container.shutdown();
            logger.info("seclog stoped.");
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

}
