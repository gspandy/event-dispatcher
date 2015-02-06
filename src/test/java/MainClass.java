import org.springframework.aop.scope.ScopedProxyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhangsx on 2015/1/26.
 */
public class MainClass {

    public static void main(String[] args) throws InterruptedException {
//        Clazz1 clazz1 = new Clazz1("");
//        String s = Clazz1.<String>parseObject("",String.class);
//        String s = "{\"AppId\":\"480523\",\"ClientId\":\"\",\"LogType\":1,\"MsgBody\":[{\"Key\":\"ServerName\",\"Value\":\"\"},{\"Key\":\"URL\",\"Value\":\"\"},{\"Key\":\"Referer\",\"Value\":\"\"},{\"Key\":\"RequestTime\",\"Value\":\"2015-02-04 20:25:21.054\"},{\"Key\":\"InsertTime\",\"Value\":\"\"},{\"Key\":\"LogId\",\"Value\":\"\"},{\"Key\":\"PID\",\"Value\":\"\"},{\"Key\":\"OID\",\"Value\":\"\"},{\"Key\":\"UserName\",\"Value\":\"test111111\"},{\"Key\":\"DID\",\"Value\":\"\"},{\"Key\":\"EID\",\"Value\":\"\"},{\"Key\":\"PageId\",\"Value\":\"\"},{\"Key\":\"VisitorId\",\"Value\":\"\"},{\"Key\":\"OperationType\",\"Value\":\"AL_F\"},{\"Key\":\"UserType\",\"Value\":\"MemberLogin\"},{\"Key\":\"PayId\",\"Value\":\"\"},{\"Key\":\"OperationContent\",\"Value\":\"\"},{\"Key\":\"URLParameter\",\"Value\":\"\"},{\"Key\":\"RespondType\",\"Value\":\"T\"},{\"Key\":\"RespondData\",\"Value\":\"\"},{\"Key\":\"Field1\",\"Value\":\"\"},{\"Key\":\"Field2\",\"Value\":\"\"},{\"Key\":\"Field3\",\"Value\":\"\"},{\"Key\":\"Field4\",\"Value\":\"\"},{\"Key\":\"Field5\",\"Value\":\"M\"},{\"Key\":\"Field6\",\"Value\":\"\"},{\"Key\":\"Field7\",\"Value\":\"\"},{\"Key\":\"Field8\",\"Value\":\"\"},{\"Key\":\"Field9\",\"Value\":\"\"},{\"Key\":\"Field10\",\"Value\":\"\"}],\"SceneType\":\"LOGIN_APPLY\",\"SourceFrom\":\"CRM\",\"SubSourceFrom\":\"LOGIN\",\"TimeStamp\":1423052721056,\"UID\":\"test111111\",\"UserIp\":\"\"}";
//        Map map = Utils.JSON.parseObject(s, Map.class);
//        String logtype = map.get("LogType").toString();
//        List body = (List) map.get("MsgBody");
//        for (int i = 0; i < body.size(); i++) {
//            String key = ((Map) body.get(0)).get("Key").toString();
//            String value = ((Map) body.get(0)).get("Value").toString();
//        }
        String s0=  "Abc";
        String s1=  "abc";
        s0.charAt(0);

    }

    private static enum SEC {
//        "1"("CP1001001"),
//        CP1001002("SEC_REGIST"),//SEC_REGIST
//        CP1001003("SEC_USER_OPERATION"),//SEC_USER_OPERATION
//        CP1001004("SEC_UBT"),//SEC_UBT
//        CP1001005("SEC_ORDER"),//SEC_ORDER
//        CP1001006("SEC_DID");//SEC_DID
        s1("");
        private String value;

        SEC(String value) {
            this.value = value;
        }

        String getValue() {
            return this.value;
        }
    }

//    static class Sync extends AbstractQueuedSynchronizer {
//        final void lock() {
//            if (compareAndSetState(0, 1))
//                setExclusiveOwnerThread(Thread.currentThread());
//            else
//                acquire(1);
//        }
//        final void unlock(){
//            release(1);
//        }
//        protected final boolean tryAcquire(int acquires) {
//            return nonfairTryAcquire(acquires);
//        }
//        protected final boolean tryRelease(int releases) {
//            int c = getState() - releases;
//            if (Thread.currentThread() != getExclusiveOwnerThread())
//                throw new IllegalMonitorStateException();
//            boolean free = false;
//            if (c == 0) {
//                free = true;
//                setExclusiveOwnerThread(null);
//            }
//            setState(c);
//            return free;
//        }
//        final boolean nonfairTryAcquire(int acquires) {
//            final Thread current = Thread.currentThread();
//            int c = getState();
//            if (c == 0) {
//                if (compareAndSetState(0, acquires)) {
//                    setExclusiveOwnerThread(current);
//                    return true;
//                }
//            }
//            else if (current == getExclusiveOwnerThread()) {
//                int nextc = c + acquires;
//                if (nextc < 0) // overflow
//                    throw new Error("Maximum lock count exceeded");
//                setState(nextc);
//                return true;
//            }
//            return false;
//        }
//    }
}
