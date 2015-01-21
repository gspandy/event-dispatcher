package com.ctrip.infosec.riskverify.biz;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.riskverify.biz.command.DroolsHystrixCommand;
import com.ctrip.infosec.riskverify.biz.command.RabbitMqHystrixCommand;
import com.ctrip.infosec.riskverify.biz.exception.ValidFailedException;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RiskVerifyBiz {
//    private static final Logger logger = LoggerFactory.getLogger(RiskVerifyBiz.class);
private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public RiskResult exe(RiskFact req,final String fact) throws ValidFailedException {
        req.setRequestReceive(sdf.format(new Date()));
        if (!Configs.isValidEventPoint(req.getEventPoint())) {
            //TODO set 403 code and retrun response
            throw new ValidFailedException();
        }
        req.setEventId(Configs.timeBasedUUID());
        req.setEventBody(Configs.normalizeEvent(req.getEventPoint(), req.getEventBody()));

        DroolsHystrixCommand drools_command = new DroolsHystrixCommand(req);
        RiskResult resp =  drools_command.execute();

        RabbitMqHystrixCommand mq_command = new RabbitMqHystrixCommand(Utils.JSON.toJSONString(resp));
        mq_command.execute();
        return resp;
    }
}
