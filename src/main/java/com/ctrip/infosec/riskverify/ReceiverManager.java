package com.ctrip.infosec.riskverify;

import java.util.List;

/**
 * Created by zhangsx on 2015/2/3.
 * 定义前端接收器的管理行为
 */
public interface ReceiverManager {
    /**
     * 添加接收器
     */
    void addReceiver();

    /**
     * 管理接收器列表中的接收器，根据Configs信息管理其是否开启关闭等行为
     */
    void manage();
}
