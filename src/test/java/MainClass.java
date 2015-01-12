import org.apache.http.client.fluent.Request;

/**
 * Created by zhangsx on 2015/1/7.
 */
public class MainClass {
    public static void main(String[] args) {
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
