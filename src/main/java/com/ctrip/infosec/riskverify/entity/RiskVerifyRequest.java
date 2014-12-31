package com.ctrip.infosec.riskverify.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by zhangsx on 2014/12/22.
 */
//@XmlRootElement(name="RiskVerifyRequest")
    public class RiskVerifyRequest {
    private String eventPoint;   //接入点
//    private long requestTime;  //用Ticks表示
//    private long receiveTime;
    private String eventId;
    private String appId;

    private Map event;

    public String getEventPoint() {
        return eventPoint;
    }

    public void setEventPoint(String eventPoint) {
        this.eventPoint = eventPoint;
    }

//    public long getRequestTime() {
//        return requestTime;
//    }
//
//    public void setRequestTime(long requestTime) {
//        this.requestTime = requestTime;
//    }
//
//    public long getReceiveTime() {
//        return receiveTime;
//    }
//
//    public void setReceiveTime(long receiveTime) {
//        this.receiveTime = receiveTime;
//    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Map getEvent() {
        return event;
    }

    public void setEvent(Map event) {
        this.event = event;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
