import com.ctrip.infosec.configs.utils.Utils;
import com.google.common.collect.Lists;
import ttt.Test0;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangsx on 2015/3/2.
 */
class ClassTest{
    public Object obj;
}
public abstract class DateTest {
    public static void main(String[] args) {
        MyClassLoader loader = new MyClassLoader();
        try {
            Class cla = loader.loadClass("");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class MyClassLoader extends ClassLoader{
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
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
        return super.loadClass(name);
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
