package standardMiddlewareImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import handlerImpl.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhangsx on 2015/2/5.
 */
public class SecStandard implements StandardMiddleware {
    @Autowired
    @Qualifier("commonHandler")
    private Handler handler;
    private final String prefix = "C";
    private static Logger logger = LoggerFactory.getLogger(SecStandard.class);
    private static MessageDigest MD5;
    private static Splitter PARAM_SPLITER;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("MessageDigest.getInstance error.", e);
        }
        PARAM_SPLITER = Splitter.on('&').omitEmptyStrings().trimResults();
    }

    @Override
    public void assembleAndSend(Map map) {
        if (map != null) {
            String fact = map.get("fact").toString();
            byte[] body = (byte[]) map.get("body");
            String body_str = new String(body, Charset.forName("utf-8"));

            Map bodyMap = Utils.JSON.parseObject(body_str, Map.class);
            String logType = Objects.toString(bodyMap.get("LogType"), "");
            String appid = Objects.toString(bodyMap.get("AppId"), "");
            String clientId = Objects.toString(bodyMap.get("ClientId"), "");
            String sceneType = Objects.toString(bodyMap.get("SceneType"), "");
            String sourceFrom = Objects.toString(bodyMap.get("SourceFrom"), "");
            String subSourceFrom = Objects.toString(bodyMap.get("SubSourceFrom"), "");
            String timeStamp = Objects.toString(bodyMap.get("TimeStamp"), "");
            String uid = Objects.toString(bodyMap.get("UID"), "");
            String userIp = Objects.toString(bodyMap.get("UserIp"), "");
            String cp = SEC.valueOf(prefix + logType).getValue();

            List<Map> msgBody = (List<Map>) bodyMap.get("MsgBody");

            RiskFact req = new RiskFact();
            Map req_body = new LinkedHashMap();
            req_body.put("logType", logType);
            req_body.put("appid", appid);
            req_body.put("clientId", clientId);
            req_body.put("sceneType", sceneType);
            req_body.put("sourceFrom", sourceFrom);
            req_body.put("subSourceFrom", subSourceFrom);
            req_body.put("timeStamp", timeStamp);
            req_body.put("uid", uid);
            req_body.put("userIp", userIp);
            for (int i = 0; i < msgBody.size(); i++) {
                String key = Objects.toString(msgBody.get(i).get("Key"), "nullKey");
                String value = Objects.toString(msgBody.get(i).get("Value"), "nullValue");
                //判断CP1001008接入点 如果是POST方式，则将key为Field2的value做md5
                if ("CP1001008".equals(cp) && "OperationType".equalsIgnoreCase(key) && "POST".equalsIgnoreCase(value)) {
                    String psw = encryptSecData(msgBody);
                    if(psw!=null){
                        req_body.put("Field2",psw);
                    }
                } else {
                    req_body.put(key, value);
                }
            }
            req.setAppId(appid);
            req.setEventPoint(cp);
            req.setEventBody(req_body);
            handler.send(ImmutableMap.of("FACT", fact, "CP", cp, "body", req));
        }
    }

    private enum SEC {
//        CP1001001("SEC_LOGIN"),//SEC_LOGIN
//        CP1001002("SEC_REGIST"),//SEC_REGIST
//        CP1001003("SEC_USER_OPERATION"),//SEC_USER_OPERATION
//        CP1001004("SEC_UBT"),//SEC_UBT
//        CP1001005("SEC_ORDER"),//SEC_ORDER
//        CP1001006("SEC_DID");//SEC_DID

        C1("CP1001001"),
        C2("CP1001002"),
        C3("CP1001003"),
        C4("CP1001004"),
        C5("CP1001005"),
        C6("CP1001006"),

        C8("CP1001008");

        private String value;

        SEC(String value) {
            this.value = value;
        }

        String getValue() {
            return this.value;
        }
    }

    private String encryptSecData(List<Map> msgBody) {
        for (Map<String, String> map : msgBody) {
            String key = map.get("Key");
            String value = map.get("Value");
            if (key!=null&&"Field2".equalsIgnoreCase(key)) {
                if (value == null) {
                    return null;
                }
//                return ImmutableMap.of("Field2", replacePW(value));
                return replacePW(value);
            }
        }
        return null;
    }


    /**
     * 加密方式
     * <p/>
     * Password=123456
     * 或
     * "Password":"E5D5845A8660D090"
     *
     * @param content
     * @return
     */


    private String replacePW(String content) {
        String key = "Password=";
        if (content.startsWith(key) == false) {
            key = "&Password=";
        }
        int index = content.indexOf(key);
        if (index >= 0) {

            StringBuilder ret = new StringBuilder();

            ret.append(content.substring(0, index + key.length()));
            int end = content.indexOf('&', index + key.length());
            if (end < 0) {
                end = content.length();
            }
            String encryptContent = content.substring(index + key.length(), end);
            for (int i = 0; i < 5; i++) {
                encryptContent = hex(MD5.digest(encryptContent.getBytes()));
            }
            ret.append(encryptContent);
            if (content.length() > end) {
                ret.append(content.substring(end, content.length()));
            }
            return ret.toString();
        } else {
            return content;
        }
    }

    public String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

}
