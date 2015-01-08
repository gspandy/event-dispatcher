package com.ctrip.infosec.riskverify.biz;

import com.ctrip.infosec.riskverify.rabbitmq.RabbitMqSender;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangsx on 2015/1/7.
 */
@Deprecated
public class Counter {

    private static Counter instance = null;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Counter.class);
    private static Object lockinstance = new Object();
    private Lock lockrest = new ReentrantLock();
    private Lock lockmq = new ReentrantLock();
    private Lock locksoa = new ReentrantLock();

    private long restcounter = 0L;
    private long mqcounter = 0L;
    private long soacounter = 0L;

    private Counter() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    lockrest.lock();
                    lockmq.lock();
                    locksoa.lock();

                    System.out.println("rest:" + restcounter);
                    System.out.println("mq:" + mqcounter);
                    System.out.println("soa:" + soacounter);

                    System.out.println("************************************");

                    restcounter = 0L;
                    mqcounter = 0L;
                    soacounter = 0L;
                } catch (Exception ex) {
                    logger.warn("Counter construct throws exception : " + ex.toString());
                } finally {
                    locksoa.unlock();
                    lockmq.unlock();
                    lockrest.unlock();
                }


            }
        }, 1000L, 1000L);

    }

    public static Counter getInstance() {
        if (instance == null) {
            synchronized (lockinstance) {
                if (instance == null) {
                    instance = new Counter();
                }
            }
        }
        return instance;
    }

    public void addrestonce() {

        try {
            lockrest.lock();
            restcounter++;
        } catch (Exception ex) {
            logger.warn("Counter addrestonce throws exception : " + ex.toString());
        } finally {
            lockrest.unlock();
        }

    }

    public void addmqonce() {

        try {
            lockmq.lock();
            mqcounter++;
        } catch (Exception ex) {
            logger.warn("Counter addmqonce throws exception : " + ex.toString());
        } finally {
            lockmq.unlock();
        }

    }

    public void addsoaonce() {

        try {
            locksoa.lock();
            soacounter++;
        } catch (Exception ex) {
            logger.warn("Counter addsoaonce throws exception : " + ex.toString());
        } finally {
            locksoa.unlock();
        }

    }
}
