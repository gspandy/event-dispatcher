package ReceiverManagerImpl;

import com.ctrip.infosec.configs.Configs;
import com.ctrip.infosec.configs.event.EventAgentName;
import com.ctrip.infosec.configs.event.EventAgentStatus;
import manager.Receiver;
import manager.ReceiverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(Manager.class);
    @Override
    public void addReceiver() {

    }

    @Override
    public void manage() {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
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
                }catch (Throwable throwable){
                    logger.error("ReceiverManager error.",throwable);
                }
            }

        }, 0L, 30L, TimeUnit.SECONDS);
    }
}
