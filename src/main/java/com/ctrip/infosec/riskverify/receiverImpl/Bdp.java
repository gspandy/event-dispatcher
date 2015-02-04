package com.ctrip.infosec.riskverify.receiverImpl;

import com.ctrip.infosec.bdp.agent.BDPAgent;
import com.ctrip.infosec.bdp.agent.ser.JsonSerImpl;
import com.ctrip.infosec.bdp.agent.slog.SLog;
import com.ctrip.infosec.riskverify.Receiver;

/**
 * Created by zhangsx on 2015/2/4.
 */
public class Bdp implements Receiver {
    @Override
    public void init() {

    }

    @Override
    public void start() {
        BDPAgent<SLog> agent = BDPAgent.createLogAgentForBDP("", "", JsonSerImpl.getInstance());

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void recovery() {

    }
}
