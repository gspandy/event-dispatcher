package handlerImpl;

import biz.RiskVerifyBiz;
import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.sars.monitor.SarsMonitorContext;
import com.ctrip.infosec.sars.monitor.util.Utils;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class CommonHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger("biz");
    @Autowired
    private RiskVerifyBiz biz;
    private FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public RiskResult verify(Channel channel, RiskFact fact) {
        fact = biz.exe(channel, fact);
        RiskResult result = transform(fact);
        return result;
    }

    public RiskFact execute(Channel channel, RiskFact fact) {
        fact = biz.exe(channel, fact);
        fact.setResponseTime(sdf.format(new Date()));
        return fact;
    }

    RiskResult transform(RiskFact fact) {
        RiskResult result = new RiskResult();
        result.setRequestReceive(fact.getRequestReceive());
        result.setEventPoint(fact.getEventPoint());
        result.setEventId(fact.getEventId());
        result.setRequestTime(fact.getRequestTime());
        result.setResponseTime(sdf.format(new Date()));
        result.setResults(fact.getFinalResult());
        return result;
    }
}
