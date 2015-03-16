import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SimpleTimeZone;
import java.util.concurrent.*;

/**
 * Created by zhangsx on 2015/3/9.
 */
public class Clazz {
    public static void main(String[] args) {
//        final ForkJoinPool forkJoinPool = new ForkJoinPool();
//        forkJoinPool.invoke(new MyRecursiveTask());

        FutureTask<String> task = new FutureTask<String>(new Callable() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "hello";
            }
        });
        Thread t = new Thread(task);
        t.start();

        try {
            System.out.println(task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class MyForkJoinTask<V> extends ForkJoinTask<V> {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private V value;

        private boolean success = false;

        @Override
        public V getRawResult() {
            return value;
        }

        @Override
        protected void setRawResult(V value) {
            this.value = value;
        }

        @Override
        protected boolean exec() {
            System.out.println("exec");
            return this.success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean isSuccess) {
            this.success = isSuccess;
        }

    }
    static class MyRecursiveTask extends RecursiveTask{
        static int i = 3;
        @Override
        protected Object compute() {
            System.out.println("1");
            i--;
            if(i<0){

            }else{

                MyRecursiveTask task0 = new MyRecursiveTask();
                MyRecursiveTask task1 = new MyRecursiveTask();

                task0.fork();
                task1.fork();

                task0.join();
                task1.join();

                System.out.println("2");
            }
            return null;
        }
    }
}

