package com.ctrip.infosec.riskverify;

/**
 * Created by zhangsx on 2015/2/3.
 * 定义前端接收器的生命周期
 */
public interface Lifecycle {
    /**
     * 初始化接收器
     */
    @Deprecated
    void init();
    /**
     * 开启接收器
     */
    void start();
    /**
     * 关闭接收器
     */
    void stop();
    /**
     * 重启接收器
     */
    void restart();

    public enum ReceiverStatus {
        init,
        running,
        stoped
    }
}
