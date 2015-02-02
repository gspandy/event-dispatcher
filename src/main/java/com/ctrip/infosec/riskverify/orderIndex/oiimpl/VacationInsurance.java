package com.ctrip.infosec.riskverify.orderIndex.oiimpl;

import com.ctrip.infosec.riskverify.orderIndex.IOrderIndex;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2015/1/29.
 */
@Component
public class VacationInsurance extends IOrderIndex{
    public VacationInsurance() {
        super("100000557_0449e2e7", "Order.Update", "OI.VacationInsurance.Order.Update", "CP0029004", "CMessage");
    }
}
