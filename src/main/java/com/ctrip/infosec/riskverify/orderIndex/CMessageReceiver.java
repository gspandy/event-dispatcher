package com.ctrip.infosec.riskverify.orderIndex;

import com.ctrip.cmessaging.client.IAsyncConsumer;
import com.ctrip.cmessaging.client.IMessage;
import com.ctrip.cmessaging.client.event.IConsumerCallbackEventHandler;
import com.ctrip.cmessaging.client.exception.IllegalExchangeName;
import com.ctrip.cmessaging.client.exception.IllegalTopic;
import com.ctrip.cmessaging.client.impl.Config;
import com.ctrip.cmessaging.client.impl.ConsumerFactory;
import org.drools.definition.rule.Global;
import org.drools.runtime.Globals;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangsx on 2015/1/26.
 */
@Deprecated
public class CMessageReceiver {

    @Autowired
    CMessageHandler handler;

    /**
     * FWS:http://ws.config.framework.fws.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     * UAT:http://ws.config.framework.uat.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     * PRD:http://ws.config.framework.sh.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/
     */
    public void init() {
//        Config.setConfigWsUri("http://ws.config.framework.fws.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/");
//        Config.setConfigWsUri("//ws.config.framework.uat.qa.nt.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/");
        Config.setConfigWsUri("http://ws.config.framework.sh.ctripcorp.com/Configws/ServiceConfig/ConfigInfoes/Get/");
        Config.setAppId("100000557");

        initImpl("OI.Flight.Order.Update");
//        initImpl("OI.Lipin.Order.Update");
//        initImpl("OI.Tuan.Order.Update");
//        initImpl("OI.Trains.Order.Update");
//        initImpl("OI.Vacation.Order.Update");
//        initImpl("OI.Piao.Order.Update");
//        initImpl("OI.DIY.Order.Update");
//        initImpl("OI.Cruise.Order.Update");
//        initImpl("OI.Car.Order.Update");
//        initImpl("OI.HHTravel.Order.Update");
//        initImpl("OI.Activity.Order.Update");
//        initImpl("OI.VacationInsurance.Order.Update");
//        initImpl("OI.Golf.Order.Update");
//        initImpl("OI.SceneryHotel.Order.Update");
//        initImpl("OI.GlobalBuy.Order.Update");
//        initImpl("OI.AirportBus.Order.Update");
//        initImpl("OI.Mall.Order.Update");
//        initImpl("OI.Bus.Order.Update");
//        initImpl("OI.Visa.Order.Update");
//        initImpl("OI.Hotel.Order.Update");
    }

    private void initImpl(String exchange) {
        String identifier = "100000557_0449e2e7";
        String subject = "Order.Update";

        IAsyncConsumer consumer = null;
        try {
            consumer = ConsumerFactory.instance.createConsumerAsAsync(identifier, subject, exchange);
        } catch (IllegalTopic illegalTopic) {
            illegalTopic.printStackTrace();
        } catch (IllegalExchangeName illegalExchangeName) {
            illegalExchangeName.printStackTrace();
        }
        if (consumer != null) {
            consumer.addConsumerCallbackEventHandler(new IConsumerCallbackEventHandler() {

                @Override
                public void callback(IMessage iMessage) throws Exception {
//                    System.out.println("***callback***");
                    handler.assembleAndSend(iMessage.getBody());
                }
            });
            consumer.setBatchSize(20);
            consumer.ConsumeAsync(5, false);
        }

    }


}
