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
    Map<String, Receiver> asyncReceivers;

    public Manager(Map<String, Receiver> asyncReceivers) {
        this.asyncReceivers = asyncReceivers;
    }

    @Override
    public void addReceiver() {

    }

    @Override
    public void manage() {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //TODO
                for (EventAgentName name : EventAgentName.values()) {
                    Receiver receiver = asyncReceivers.get(name.name());
                    if (receiver != null) {
                        EventAgentStatus status = Configs.getEventAgentStatus(name);
                        if (status == EventAgentStatus.DISABLED) {
                            receiver.stop();
                        }
                        if (status == EventAgentStatus.ENABLED) {
                            receiver.start();
                        }
                    }
                }
            }

        }, 0L, 30L, TimeUnit.SECONDS);
    }
}
