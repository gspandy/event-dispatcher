package com.ctrip.infosec.riskverify.orderIndex.oiimpl;

import com.ctrip.cmessaging.client.IMessage;
import com.ctrip.infosec.riskverify.orderIndex.IOrderIndex;

/**
 * Created by zhangsx on 2015/1/29.
 */
public class Trains extends IOrderIndex{
    public Trains() {
        super("100000557_0449e2e7", "Order.Update", "OI.Flight.Order.Update", "CP0012004", "CMessage");
    }

    @Override
    public void handleMessage(IMessage message) {
        assembleAndSend(message.getBody());
    }
}
