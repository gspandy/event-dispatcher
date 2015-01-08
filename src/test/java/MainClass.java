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

//        for (int i = 0; i < 10; i++) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
////                    for(int j=0;j<1000000;j++){
//                    for(;;){
//                        Counter.getInstance().addmqonce();
//                    }
//                }
//            });
//            t.start();
//        }

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
        Clazz.func();
    }

    static class Clazz {
        static void func() throws RuntimeException{
            try{
                throw new Exception("test exception");
            }catch (Throwable ex){
                System.out.print(ex);
                throw new RuntimeException();
            }
        }
    }
}
