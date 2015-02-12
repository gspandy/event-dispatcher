<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!--Output to central logging -->
    <appender name="CentralLogging"
              class="com.ctrip.freeway.appender.CentralLoggingAppender">
        <appId>100000557</appId>
        <serverIp>{$ClogIp}</serverIp>
        <serverPort>{$ClogPort}</serverPort>
    </appender>

    <appender name="fileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoding>UTF-8</encoding>
        <file>/opt/logs/tomcat/eventdispatcher.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/opt/logs/tomcat/eventdispatcher.log.%d{yyyy-MM-dd}</fileNamePattern>
            <maxHistory>7</maxHistory>
        </rollingPolicy>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>%date %-5level %logger{40} - %msg%n</pattern>
        </layout>
    </appender>
    <root level="INFO">
        <appender-ref ref="fileAppender"/>
        <appender-ref ref="CentralLogging"/>
    </root>

</configuration>