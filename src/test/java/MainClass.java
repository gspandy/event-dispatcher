import com.ctrip.infosec.riskverify.biz.Counter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangsx on 2015/1/7.
 */
public class MainClass {
    public static void main(String[] args) {

//        Executors.newFixedThreadPool(10).execute(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//
//                    Counter.getInstance().addmqonce();
//                }
//            }
//        });
//
//        Executors.newFixedThreadPool(10).execute(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    Counter.getInstance().addrestonce();
//                }
//            }
//        });
//        Executors.newFixedThreadPool(10).execute(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    Counter.getInstance().addsoaonce();
//                }
//            }
//        });
//
//

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
//                    for(int j=0;j<1000000;j++){
                    for(;;){
                        Counter.getInstance().addmqonce();
                    }
                }
            });
            t.start();
        }

//        for (int i = 0; i < 10; i++) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for(int j=0;j<1000000;j++){
//                        Counter.getInstance().addrestonce();
//                    }
//                }
//            });
//            t.start();
//        }
//
//        for (int i = 0; i < 10; i++) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for(int j=0;j<1000000;j++){
//                        Counter.getInstance().addsoaonce();
//                    }
//                }
//            });
//            t.start();
//        }

    }

    static class Clazz {
        public Clazz() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("1");
                }
            }, 0L,1000L);

        }
        public void func() {
            System.out.println("12");
        }
    }
}
