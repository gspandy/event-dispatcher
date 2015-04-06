<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="orderIndexStandard" class="standardMiddlewareImpl.OrderIndexStandard"/>
    <bean id="secStandard" class="standardMiddlewareImpl.SecStandard"/>
       
    <bean id="OrderIndex_Flight" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Flight.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0011004"/>
    </bean>

    <bean id="OrderIndex_Activity" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Activity.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0023004"/>
    </bean>

    <bean id="OrderIndex_Lipin" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Lipin.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0002004"/>
    </bean>

    <bean id="OrderIndex_Tuan" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Tuan.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0033004"/>
    </bean>

    <bean id="OrderIndex_Trains" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Trains.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0012004"/>
    </bean>

    <bean id="OrderIndex_Vacation" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Vacation.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0028004"/>
    </bean>


    <bean id="OrderIndex_Piao" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Piao.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0022004"/>
    </bean>

    <bean id="OrderIndex_DIY" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.DIY.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0027004"/>
    </bean>

    <bean id="OrderIndex_Cruise" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Cruise.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0026004"/>
    </bean>

    <bean id="OrderIndex_Car" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Car.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0024004"/>
    </bean>

    <bean id="OrderIndex_HHTravel" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.HHTravel.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0021004"/>
    </bean>

    <bean id="OrderIndex_VacationInsurance"
          class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.VacationInsurance.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0029004"/>
    </bean>

    <bean id="OrderIndex_Golf" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Golf.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0025004"/>
    </bean>

    <bean id="OrderIndex_SceneryHotel" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.SceneryHotel.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0032004"/>
    </bean>

    <bean id="OrderIndex_GlobalBuy" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.GlobalBuy.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0005004"/>
    </bean>

    <bean id="OrderIndex_AirportBus" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.AirportBus.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0015004"/>
    </bean>

    <bean id="OrderIndex_Mall" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Mall.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0003004"/>
    </bean>

    <bean id="OrderIndex_Bus" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Bus.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0014004"/>
    </bean>

    <bean id="OrderIndex_Visa" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Visa.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0031004"/>
    </bean>

    <bean id="OrderIndex_Hotel" class="receiverImpl.CMessage">
        <constructor-arg name="identifier" value="100000557_0449e2e7"/>
        <constructor-arg name="subject" value="Order.Update"/>
        <constructor-arg name="exchange" value="OI.Hotel.Order.Update"/>
        <constructor-arg name="eventPoint" value="CP0031004"/>
    </bean>

    <!--<bean id="mqsender" class="RabbitMqSender" init-
    method="init"/>-->

    <bean class="receiverImpl.RabbitMq" init-method="start"/>
    <bean id="SEC_LOG" class="receiverImpl.SecLog"/>

    <bean class="ReceiverManagerImpl.Manager" init-method="manage">
        <constructor-arg name="asyncReceivers">
            <map key-type="java.lang.String" value-type="manager.Receiver">
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