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

/**
 * Created by zhangsx on 2015/2/3.
 */
public class OrderIndexStandard implements StandardMiddleware {

    @Autowired
    @Qualifier("commonHandler")
    private Handler handler;

    public void assembleAndSend(Channel channel, String eventPoint, byte[] body) {

        Map bodyMap = Utils.JSON.parseObject(new String(body, Charset.forName("utf-8")), Map.class);
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
    }
}
