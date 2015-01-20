package com.ctrip.infosec.riskverify.rabbitmq;

import com.ctrip.infosec.common.model.RiskFact;
import static com.ctrip.infosec.configs.utils.Utils.JSON;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.exception.ValidFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangsx on 2015/1/12.
 */
public class RabbitMqMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqMessageHandler.class);
    private final String FACT = "MQ";
    @Autowired
    private RiskVerifyBiz biz;

    public void handleMessage(String req) {
        logger.info("MQ: " + req);
        try {
            RiskFact obj = JSON.parseObject(req, RiskFact.class);
            biz.exe(obj, FACT);
        } catch (ValidFailedException e) {
            logger.warn("http 403 exception");
        } catch (Exception ex) {
            logger.warn(ex.toString());
        } catch (Throwable t) {
            logger.error(t.toString());
        }
    }
}
