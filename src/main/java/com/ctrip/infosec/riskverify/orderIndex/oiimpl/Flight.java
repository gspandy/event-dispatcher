package com.ctrip.infosec.riskverify.orderIndex.oiimpl;

import com.ctrip.cmessaging.client.IMessage;
import com.ctrip.infosec.riskverify.orderIndex.IOrderIndex;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2015/1/29.
 */
@Component
public class Flight extends IOrderIndex {
    public Flight() {
        super("100000557_0449e2e7", "Order.Update", "OI.Flight.Order.Update", "CP0011004", "CMessage");
    }

//    @Override
//    public void handleMessage(IMessage message) {
//        assembleAndSend(message.getBody());
//    }
}
