package com.ctrip.infosec.riskverify.ReceiverManagerImpl;

import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.event.EventAgentName;
import com.ctrip.infosec.configs.event.EventAgentStatus;
import com.ctrip.infosec.riskverify.Receiver;
import com.ctrip.infosec.riskverify.ReceiverManager;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangsx on 2015/2/4.
 */
public class Manager implements ReceiverManager {
    //    List<Map<String,Receiver>> syncReceivers;
    Map<String, Receiver> asyncReceivers;

    public Manager(Map<String, Receiver> asyncReceivers) {
        this.asyncReceivers = asyncReceivers;
    }

    @Override
    public void addReceiver() {

    }

    //    OrderIndex_Flight,
//    OrderIndex_Lipin,
//    OrderIndex_Tuan,
//    OrderIndex_Trains,
//    OrderIndex_Vacation,
//    OrderIndex_Piao,
//    OrderIndex_DIY,
//    OrderIndex_Cruise,
//    OrderIndex_Car,
//    OrderIndex_HHTravel,
//    OrderIndex_Activity,
//    OrderIndex_VacationInsurance,
//    OrderIndex_Golf,
//    OrderIndex_SceneryHotel,
//    OrderIndex_GlobalBuy,
//    OrderIndex_AirportBus,
//    OrderIndex_Mall,
//    OrderIndex_Bus,
//    OrderIndex_Visa,
//    OrderIndex_Hotel;
    @Override
    public void manage() {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //TODO
                for (EventAgentName name : EventAgentName.values()) {
                    EventAgentStatus status = Configs.getEventAgentStatus(name);
                    if (status == EventAgentStatus.DISABLED) {
                        asyncReceivers.get(name.name()).stop();
                    }
                    if (status == EventAgentStatus.ENABLED) {
                        asyncReceivers.get(name.name()).start();

                    }
                }
            }

        }, 0L, 30L, TimeUnit.SECONDS);
    }
}
