package standardMiddlewareImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.util.Utils;
import handlerImpl.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class OrderIndexStandard implements StandardMiddleware {

    private static final Logger logger = LoggerFactory.getLogger(OrderIndexStandard.class);
    @Autowired
    @Qualifier("commonHandler")
    private Handler handler;

    public void assembleAndSend(Channel channel, String eventPoint, byte[] body) {

        String bodyTxt = new String(body, Charset.forName("utf-8"));
        if (StringUtils.startsWith(bodyTxt, "{") && StringUtils.endsWith(bodyTxt, "}")) {
            Map bodyMap = Utils.JSON.parseObject(bodyTxt, Map.class);
            List<Map> subjects = (List<Map>) bodyMap.get("Subjects");
            for (Map item : subjects) {
                RiskFact fact = new RiskFact();
                fact.setEventPoint(eventPoint);

                Map _map = new HashMap();
                for (Object o : item.keySet()) {
                    String key = o.toString();
                    String first = key.substring(0, 1).toLowerCase();
                    String rest = key.substring(1, key.length());
                    _map.put(first + rest, item.get(key));
                }
                fact.setEventBody(_map);
                handler.send(channel, fact);
            }
        } else {
            logger.warn("CMessage[" + eventPoint + "] Base64Body: " + Base64.encodeBase64String(body));
        }
    }
}
