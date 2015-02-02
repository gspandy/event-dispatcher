<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="com.ctrip.infosec.riskverify"/>
    <context:annotation-config/>
    <mvc:annotation-driven/>

    <!--监控-->
    <bean id="sarsMonitorAdvice" class="com.ctrip.infosec.riskverify.aop.RiskVerifyAdvice"/>
    <aop:config proxy-target-class="true">
        <aop:advisor advice-ref="sarsMonitorAdvice"
                     pointcut="execution(* com.ctrip.infosec.riskverify.biz.RiskVerifyBiz.exe(..))"/>
        <!--<aop:advisor advice-ref="sarsMonitorAdvice" pointcut="execution(* com.ctrip.infosec.riskverify.rest.RiskRestImpl.checkHealth(..))"/>-->
    </aop:config>
    <bean class="com.ctrip.infosec.sars.monitor.scheduler.SarsMonitorScheduler" init-method="start">
        <property name="appId" value="100000557"/>
        <property name="postUrl" value="{$MONITOR}"/>
    </bean>

    <bean class="com.ctrip.infosec.configs.Caches" init-method="init">
        <property name="url" value="{$DATACONFI_URL}"/>
        <property name="part" value="EventDispatcher"/>
    </bean>

    <bean class="com.ctrip.infosec.riskverify.biz.rabbitmq.RabbitMqHandler" init-method="init"/>

</beans>