import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zhangsx on 2015/3/9.
 */
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        System.out.println(name);
        if("1".equals(name)){
            try {
                FileInputStream fis = new FileInputStream("E:\\GIT_WORKSPACE\\event-dispatcher\\infosec-riskverify-service-core\\target\\test-classes\\A.class");
                byte[] bytes = new byte[1024];
                try {
                    int size = fis.read(bytes);
                    return defineClass("A",bytes,0,size);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return super.loadClass(name, resolve);
    }
}
