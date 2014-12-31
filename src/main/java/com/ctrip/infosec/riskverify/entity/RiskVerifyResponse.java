package com.ctrip.infosec.riskverify.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by zhangsx on 2014/12/22.
 */
//custor
public class RiskVerifyResponse {
    private String eventPoint;   //接入点
    private String eventId;
    private String appId;
    private Map event;
    private Map results;
    private Map finalResult;
    private Map ext;
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

    public Map getEvent() {
        return event;
    }

    public void setEvent(Map event) {
        this.event = event;
    }

    public Map getResults() {
        return results;
    }

    public void setResults(Map results) {
        this.results = results;
    }

    public Map getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Map finalResult) {
        this.finalResult = finalResult;
    }

    public Map getExt() {
        return ext;
    }

    public void setExt(Map ext) {
        this.ext = ext;
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
