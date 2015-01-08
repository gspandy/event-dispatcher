package com.ctrip.infosec.riskverify.entity;

import java.util.Map;

/**
 * Created by zhangsx on 2014/12/26.
 */
@Deprecated
public class RiskVerifyResponse {
    private String eventPoint;   //接入点
    private String eventId;
    private String appId;
    private Map results;
    private long requestTime;  //用Ticks表示
    private long receiveTime;

    public String getEventPoint() {
        return eventPoint;
    }

    public void setEventPoint(String eventPoint) {
        this.eventPoint = eventPoint;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Map getResults() {
        return results;
    }

    public void setResults(Map results) {
        this.results = results;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
}
