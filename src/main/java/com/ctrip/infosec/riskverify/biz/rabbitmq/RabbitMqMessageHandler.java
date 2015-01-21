package com.ctrip.infosec.riskverify.biz.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangsx on 2015/1/12.
 */
@Deprecated
public class RabbitMqMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqMessageHandler.class);
    private final String FACT = "MQ";
//    @Autowired
//    private RiskVerifyBiz biz;

    public void handleMessage(String req) {

        logger.info("MQ: " + req);
        try {
//            RiskFact obj = JSON.parseObject(req, RiskFact.class);
//            biz.exe(obj, FACT);

            System.out.println(req);

//            biz.exe((RiskFact)req,FACT);
//        } catch (ValidFailedException e) {
//            logger.warn("http 403 exception");
        } catch (Exception ex) {
            logger.warn(ex.toString());
        } catch (Throwable t) {
            logger.error(t.toString());
        }
    }
}
