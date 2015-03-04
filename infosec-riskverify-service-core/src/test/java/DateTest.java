import ttt.Test0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zhangsx on 2015/3/2.
 */
public abstract class DateTest {
    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Class cl = myClassLoader.loadClass("");
            try {
                System.out.println(cl.getClass().getName());
                Object obj = cl.getClass().newInstance();
                System.out.println(obj instanceof Test0);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class MyClassLoader extends ClassLoader{
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(name.startsWith("java.")){
            super.loadClass(name);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream("E:\\GIT_WORKSPACE\\event-dispatcher\\infosec-riskverify-service-core\\target\\test-classes\\ttt\\Test0.class");
            byte[] bytes = new byte[1024];
            try {
                int size = fileInputStream.read(bytes);
                return defineClass("ttt.Test0",bytes,0,size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

//    @Override
//    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        try {
//            FileInputStream fileInputStream = new FileInputStream("E:\\GIT_WORKSPACE\\event-dispatcher\\infosec-riskverify-service-core\\target\\test-classes\\ttt\\Test0.class");
//            byte[] bytes = new byte[1024];
//            try {
//                int size = fileInputStream.read(bytes);
//                return defineClass("ttt.Test0",bytes,0,size);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }throw new RuntimeException();
//    }
}
