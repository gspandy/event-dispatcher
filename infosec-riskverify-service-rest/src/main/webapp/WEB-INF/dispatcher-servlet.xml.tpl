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
        <aop:advisor advice-ref="sarsMonitorAdvice" pointcut="execution(* com.ctrip.infosec.riskverify.biz.RiskVerifyBiz.exe(..))"/>
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
    <bean class="com.ctrip.infosec.configs.event.monitor.EventCounterSender" init-method="start">
        <property name="url" value="{$EVENT_MONITOR_URL}"/>
    </bean>

    <bean id="orderIndexStandard" class="com.ctrip.infosec.riskverify.standardMiddlewareImpl.OrderIndexStandard"/>
    <bean id="secStandard" class="com.ctrip.infosec.riskverify.standardMiddlewareImpl.SecStandard"/>

    <bean class="com.ctrip.infosec.riskverify.handlerImpl.CommonHandler"/>
    <bean id="OrderIndex_Flight" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Flight.Order.Update"/>
        <constructor-arg name="cp" value="CP0011004"/>
    </bean>

    <bean id="OrderIndex_Activity" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Activity.Order.Update"/>
        <constructor-arg name="cp" value="CP0023004"/>
    </bean>

    <bean id="OrderIndex_Lipin" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Lipin.Order.Update"/>
        <constructor-arg name="cp" value="CP0002004"/>
    </bean>

    <bean id="OrderIndex_Tuan" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Tuan.Order.Update"/>
        <constructor-arg name="cp" value="CP0033004"/>
    </bean>

    <bean id="OrderIndex_Trains" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Trains.Order.Update"/>
        <constructor-arg name="cp" value="CP0012004"/>
    </bean>

    <bean id="OrderIndex_Vacation" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Vacation.Order.Update"/>
        <constructor-arg name="cp" value="CP0028004"/>
    </bean>

    <bean id="OrderIndex_Piao" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Piao.Order.Update"/>
        <constructor-arg name="cp" value="CP0022004"/>
    </bean>

    <bean id="OrderIndex_DIY" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.DIY.Order.Update"/>
        <constructor-arg name="cp" value="CP0027004"/>
    </bean>

    <bean id="OrderIndex_Cruise" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Cruise.Order.Update"/>
        <constructor-arg name="cp" value="CP0026004"/>
    </bean>

    <bean id="OrderIndex_Car" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Car.Order.Update"/>
        <constructor-arg name="cp" value="CP0024004"/>
    </bean>

    <bean id="OrderIndex_HHTravel" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.HHTravel.Order.Update"/>
        <constructor-arg name="cp" value="CP0021004"/>
    </bean>

    <bean id="OrderIndex_VacationInsurance" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.VacationInsurance.Order.Update"/>
        <constructor-arg name="cp" value="CP0029004"/>
    </bean>

    <bean id="OrderIndex_Golf" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Golf.Order.Update"/>
        <constructor-arg name="cp" value="CP0025004"/>
    </bean>

    <bean id="OrderIndex_SceneryHotel" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.SceneryHotel.Order.Update"/>
        <constructor-arg name="cp" value="CP0032004"/>
    </bean>

    <bean id="OrderIndex_GlobalBuy" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.GlobalBuy.Order.Update"/>
        <constructor-arg name="cp" value="CP0005004"/>
    </bean>

    <bean id="OrderIndex_AirportBus" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.AirportBus.Order.Update"/>
        <constructor-arg name="cp" value="CP0015004"/>
    </bean>

    <bean id="OrderIndex_Mall" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Mall.Order.Update"/>
        <constructor-arg name="cp" value="CP0003004"/>
    </bean>

    <bean id="OrderIndex_Bus" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Bus.Order.Update"/>
        <constructor-arg name="cp" value="CP0014004"/>
    </bean>

    <bean id="OrderIndex_Visa" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Visa.Order.Update"/>
        <constructor-arg name="cp" value="CP0031004"/>
    </bean>

    <bean id="OrderIndex_Hotel" class="com.ctrip.infosec.riskverify.receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Hotel.Order.Update"/>
        <constructor-arg name="cp" value="CP0031004"/>
    </bean>

    <bean id="mqsender" class="com.ctrip.infosec.riskverify.biz.rabbitmq.RabbitMqSender" init-method="init"/>
    <bean class="com.ctrip.infosec.riskverify.receiverImpl.RabbitMq" init-method="start"/>
    <bean id="SEC_LOG" class="com.ctrip.infosec.riskverify.receiverImpl.SecLog"/>

    <bean class="com.ctrip.infosec.riskverify.ReceiverManagerImpl.Manager" init-method="manage">
        <constructor-arg name="asyncReceivers">
            <map key-type="java.lang.String" value-type="com.ctrip.infosec.riskverify.Receiver">
                <entry key="OrderIndex_Flight" value-ref="OrderIndex_Flight"></entry>
                <entry key="OrderIndex_Activity" value-ref="OrderIndex_Activity"></entry>
                <entry key="OrderIndex_Lipin" value-ref="OrderIndex_Lipin"></entry>
                <entry key="OrderIndex_Tuan" value-ref="OrderIndex_Tuan"></entry>
                <entry key="OrderIndex_Trains" value-ref="OrderIndex_Trains"></entry>
                <entry key="OrderIndex_Vacation" value-ref="OrderIndex_Vacation"></entry>
                <entry key="OrderIndex_Piao" value-ref="OrderIndex_Piao"></entry>
                <entry key="OrderIndex_DIY" value-ref="OrderIndex_DIY"></entry>
                <entry key="OrderIndex_Cruise" value-ref="OrderIndex_Cruise"></entry>
                <entry key="OrderIndex_Car" value-ref="OrderIndex_Car"></entry>
                <entry key="OrderIndex_HHTravel" value-ref="OrderIndex_HHTravel"></entry>
                <entry key="OrderIndex_VacationInsurance" value-ref="OrderIndex_VacationInsurance"></entry>
                <entry key="OrderIndex_Golf" value-ref="OrderIndex_Golf"></entry>
                <entry key="OrderIndex_SceneryHotel" value-ref="OrderIndex_SceneryHotel"></entry>
                <entry key="OrderIndex_GlobalBuy" value-ref="OrderIndex_GlobalBuy"></entry>
                <entry key="OrderIndex_AirportBus" value-ref="OrderIndex_AirportBus"></entry>
                <entry key="OrderIndex_Mall" value-ref="OrderIndex_Mall"></entry>
                <entry key="OrderIndex_Bus" value-ref="OrderIndex_Bus"></entry>
                <entry key="OrderIndex_Visa" value-ref="OrderIndex_Visa"></entry>
                <entry key="OrderIndex_Hotel" value-ref="OrderIndex_Hotel"></entry>
                <entry key="SEC_LOG" value-ref="SEC_LOG"></entry>
            </map>
        </constructor-arg>
    </bean>

</beans>