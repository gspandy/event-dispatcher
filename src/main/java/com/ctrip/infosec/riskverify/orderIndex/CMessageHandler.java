package com.ctrip.infosec.riskverify.orderIndex;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangsx on 2015/1/26.
 */
@Component
public class CMessageHandler {
    @Autowired
    private RiskVerifyBiz biz;
    private final String FACT = "CMessage";

    public CMessageHandler() {

    }

    public void assembleAndSend(byte[] body) {
//        System.out.println(new String(body, Charset.forName("utf-8")));
        Map map = Utils.JSON.parseObject(new String(body, Charset.forName("utf-8")), Map.class);
        List<Map> subjects = (List<Map>) map.get("Subjects");
        for (Map item : subjects) {
            RiskFact req = new RiskFact();
            req.setEventPoint("CP0001004");
            req.setEventBody(item);
            biz.exe(req, FACT);
        }
    }
}
