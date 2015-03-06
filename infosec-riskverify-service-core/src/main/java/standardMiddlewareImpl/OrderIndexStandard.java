package standardMiddlewareImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.collect.ImmutableMap;
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
    public void assembleAndSend(Map map) {
        if (map != null) {
            String fact = map.get("fact").toString();
            String cp = map.get("CP").toString();
            byte[] body = (byte[]) map.get("body");
            Map bodyMap = Utils.JSON.parseObject(new String(body, Charset.forName("utf-8")), Map.class);
            List<Map> subjects = (List<Map>) bodyMap.get("Subjects");
            for (Map item : subjects) {
                RiskFact req = new RiskFact();
                req.setEventPoint(cp);

                Map _map = new HashMap();
                for (Object o : item.keySet()) {
                    String key = o.toString();
                    String first = key.substring(0, 1).toLowerCase();
                    String rest = key.substring(1, key.length());
                    _map.put(first + rest, item.get(key));
                }
                req.setEventBody(_map);
                handler.send(ImmutableMap.of("FACT", fact,"CP",cp, "body", req));
            }
        }

    }
}

