package com.ctrip.infosec.riskverify.orderIndex.oiimpl;

import com.ctrip.infosec.riskverify.orderIndex.IOrderIndex;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2015/1/29.
 */
@Component
public class DIY extends IOrderIndex{
    public DIY() {
        super("100000557_0449e2e7", "Order.Update", "OI.DIY.Order.Update", "CP0027004", "CMessage");
    }
}
