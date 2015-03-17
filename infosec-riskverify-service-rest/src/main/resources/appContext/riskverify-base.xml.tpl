<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

       <context:component-scan base-package="biz"/>
       <context:annotation-config/>
       <mvc:annotation-driven/>

       <!--监控-->
       <bean id="sarsMonitorAdvice" class="aop.RiskVerifyAdvice"/>
       <aop:config proxy-target-class="true">
              <aop:advisor advice-ref="sarsMonitorAdvice" pointcut="execution(* biz.RiskVerifyBiz.exe(..))"/>
       </aop:config>

       <bean class="com.ctrip.infosec.sars.monitor.scheduler.SarsMonitorScheduler" init-method="start">
              <property name="appId" value="100000557"/>
              <property name="postUrl" value="{$MONITOR}"/>
       </bean>

       <bean class="com.ctrip.infosec.configs.ConfigsDeamon" init-method="start">
              <property name="url" value="{$DATACONFI_URL}"/>
              <property name="part" value="EventDispatcher"/>
       </bean>

       <bean class="com.ctrip.infosec.configs.event.monitor.EventCounterSender" init-method="start">
              <property name="url" value="{$EventCounter}"/>
       </bean>
       <bean id="commonHandler" class="handlerImpl.CommonHandler"/>

       <!--大安-->
       <bean id="connectionFactory0"
             class="org.springframework.amqp.rabbit.connection.CachingConnectionFactory">
              <property name="host" value="{$SecLogHost}"/>
              <property name="virtualHost" value="{$SecLogVirtualHost}"/>
              <property name="username" value="{$SecLogUsername}"/>
              <property name="password" value="{$SecLogPassword}"/>
              <property name="channelCacheSize" value="10"/>
       </bean>

       <!--Event-->
       <bean id="connectionFactory1"
             class="org.springframework.amqp.rabbit.connection.CachingConnectionFactory">
              <property name="host" value="{$EventHost}"/>
              <property name="virtualHost" value="{$EventVirtualHost}"/>
              <property name="username" value="{$EventUsername}"/>
              <property name="password" value="{$EventPassword}"/>
       </bean>

       <!--Inner-->
       <bean id="connectionFactory2"
              class="org.springframework.amqp.rabbit.connection.CachingConnectionFactory">
              <property name="host" value="{$InnerHost}"/>
              <property name="virtualHost" value="{$InnerVirtualHost}"/>
              <property name="username" value="{$InnerUsername}"/>
              <property name="password" value="{$InnerPassword}"/>
        </bean>

       <bean id="mqsender" class="org.springframework.amqp.rabbit.core.RabbitTemplate">
              <constructor-arg name="connectionFactory" ref="connectionFactory2"/>
       </bean>

</beans>