package com.ctrip.infosec.riskverify.biz;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.riskverify.command.DroolsHystrixCommand;
import com.ctrip.infosec.riskverify.entity.RiskVerifyEventData;
import com.ctrip.infosec.riskverify.exception.ValidFailedException;
import com.ctrip.infosec.riskverify.rabbitmq.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RiskVerifyBiz {
    private static final Logger logger = LoggerFactory.getLogger(RiskVerifyBiz.class);
    @Autowired
    private RabbitMqSender sender;
    public void sendToMQ(String msg) {
        sender.Producer(msg);
    }
    public RiskFact restSender(RiskFact req) {
        DroolsHystrixCommand command = new DroolsHystrixCommand(req);
        return command.execute();
    }

    public RiskFact exe(RiskFact req) throws ValidFailedException {
        req.setReceiveTime(System.nanoTime());
        if (!Configs.isValidEventPoint(req.getEventPoint())) {
            //TODO set 403 code and retrun response
            throw new ValidFailedException();
        }
        req.setEventId(Configs.timeBasedUUID());
        req.setEvent(Configs.normalizeEvent(req.getEventPoint(), req.getEvent()));
        RiskFact resp = restSender(req);
        sendToMQ(Utils.JSON.toJSONString(resp));
        return resp;
    }
}
