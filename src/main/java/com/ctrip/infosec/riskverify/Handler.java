package com.ctrip.infosec.riskverify;

import com.ctrip.infosec.common.model.RiskResult;

import java.util.Map;

/**
 * Created by zhangsx on 2015/2/3.
 * 处理标准化好的数据
 */
public interface Handler {
//    void addSourcePipeline(Pipeline source);
//    void work();
public RiskResult send(Map map);
}
