package com.ctrip.infosec.riskverify.biz;
import com.ctrip.infosec.riskverify.entity.RiskVerifyEventData;
import com.ctrip.infosec.riskverify.rabbitmq.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Component
public class RiskVerifyBiz {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);
    @Autowired
    private RabbitMqSender sender;
    @Autowired
    private RestTemplate template;

    public void sendToRuleEngine() {

    }

    public void sendToMQ(String msg) {
        sender.Producer(msg);
    }

    public ResponseEntity<RiskVerifyEventData> restSender(RiskVerifyEventData req) {
        try {
            MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
            header.add("Content-Type", "application/json");
            header.add("Accept-Encoding", "utf-8");
            HttpEntity<RiskVerifyEventData> requestEntity = new HttpEntity<RiskVerifyEventData>(req, header);
            ResponseEntity<RiskVerifyEventData> resp = template.exchange("http://10.3.6.218:8090/rule/query", HttpMethod.POST, requestEntity, RiskVerifyEventData.class);
            return resp;
        }
        catch (Exception ex){
            logger.debug("httpclient to drools occur exception");
        }
        return new ResponseEntity<RiskVerifyEventData>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    static class Test{
        public static void main(String[] args){
            try {
                RestTemplate template = new RestTemplate();
                MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
                header.add("Content-Type", "application/json");
                header.add("Accept-Encoding", "utf-8");
                RiskVerifyEventData req1 = new RiskVerifyEventData();
                req1.setAppId("com.ctrip.rule.test");
                req1.setEventId("2bc05cd2-6e04-46a3-9d67-625f5908b865");
                req1.setEventPoint("EP1");
                Map map = new HashMap();
                map.put("amount","1000");
                req1.setEvent(map);

                HttpEntity<RiskVerifyEventData> requestEntity = new HttpEntity<RiskVerifyEventData>(req1, header);
                ResponseEntity<Map> responseEntity = template.exchange("http://10.3.6.218:8090/rule/query", HttpMethod.POST, requestEntity, Map.class);
            }
            catch (Exception ex){
            }
        }
    }

}
