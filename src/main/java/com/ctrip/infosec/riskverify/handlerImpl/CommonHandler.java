package com.ctrip.infosec.riskverify.handlerImpl;

import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.riskverify.Handler;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class CommonHandler implements Handler {
    @Autowired
    private RiskVerifyBiz biz;

    @Override
    public RiskResult send(Map map) {
        return biz.exe(map);
    }
}
