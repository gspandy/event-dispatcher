import ch.qos.logback.core.ConsoleAppender;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.BatchingStrategy;
import org.springframework.amqp.rabbit.core.support.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import java.awt.*;
import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhangsx on 2015/1/26.
 */
public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setChannelCacheSize(10);
        factory.setHost("10.3.6.42");
        factory.setVirtualHost("medusa");
        factory.setUsername("bsc-medusa");
        factory.setPassword("bsc-medusa");
//        final AmqpTemplate template = new RabbitTemplate(factory);

//        template.send("ex_zsx_test","test",new Message("hello".getBytes(),new MessageProperties()));

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addQueues(new Queue("zsx_test"));
        container.setMessageListener(new MyMessageListener());
        container.setMaxConcurrentConsumers(Runtime.getRuntime().availableProcessors());

        container.start();
    }
    static class MyMessageListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            System.out.println(new String(message.getBody()));
        }
    }
    public static Object walk(Object container, ContentVisitor visitor) {
        if (container instanceof Collection) {
            Collection _container = (Collection) container;
            Collection result = new LinkedList();
            for (Object o : _container) {
                result.add(walk(o, visitor));
            }
            return result;
        }

        if (container instanceof Map) {
            Map _container = (Map) container;
            Map result = new LinkedHashMap();

            for (Object entry : _container.entrySet()) {
                Map.Entry _entry = (Map.Entry) entry;
                String lowerCaseKey = visitor.onVisitSimpleNode(_entry.getKey().toString());
                result.put(lowerCaseKey,walk(_entry.getValue(),visitor));
            }
            return result;
        }
        return container;
    }


    public interface ContentVisitor {
        String onVisitSimpleNode(String key);
    }

}
