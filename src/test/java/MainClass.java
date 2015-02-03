import com.ctrip.infosec.sars.monitor.util.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.lang.math.RandomUtils;

import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * Created by zhangsx on 2015/1/26.
 */
public class MainClass {

    public static void main(String[] args) {
        Clazz1 clazz1 = new Clazz1("");

    }

    static class Clazz0{
        public Clazz0(String msg){
            System.out.println(msg);
        }
        public void sync(){

            System.out.println(this.getClass());
        }
    }

    static class Clazz1 extends Clazz0{
        public <T> T parseObject(String jsonString, Class<T> clazz) {
            return null;
        }
        public Clazz1(String msg) {
            super(msg);
            sync();
        }
    }
}
