<?xml version="1.0" encoding="UTF-8"?>
<configuration>
<!--Output to central logging -->
<appender name="CentralLogging"
class="com.ctrip.freeway.appender.CentralLoggingAppender">
<appId>100000557</appId>
<serverIp>{$ClogIp}</serverIp>
<serverPort>{$ClogPort}</serverPort>
</appender>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoding>UTF-8</encoding>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>%date %-5level %logger{40} - %msg%n</pattern>
        </layout>
    </appender>
<root level="INFO">
    <appender-ref ref ="STDOUT"/>
<appender-ref ref="CentralLogging" />
</root>

</configuration>