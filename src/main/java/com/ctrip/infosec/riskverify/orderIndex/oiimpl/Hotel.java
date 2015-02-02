package com.ctrip.infosec.riskverify.orderIndex.oiimpl;

import com.ctrip.cmessaging.client.IMessage;
import com.ctrip.infosec.riskverify.orderIndex.IOrderIndex;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2015/1/29.
 */
@Component
public class Hotel extends IOrderIndex {

    public Hotel() {
        super("100000557_0449e2e7", "Order.Update", "OI.Hotel.Order.Update", "CP0031004", "CMessage");
    }

//    @Override
//    public void handleMessage(IMessage message) {
//        assembleAndSend(message.getBody());
//    }
}
