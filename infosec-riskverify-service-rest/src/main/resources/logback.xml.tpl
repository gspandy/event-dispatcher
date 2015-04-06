<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <substitutionProperty name="default_pattern" value="%date %-5level %logger{40} - %msg%n" />

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoding>UTF-8</encoding>
                <encoder><!-- 必须指定，否则不会往文件输出内容 -->
                    <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n</pattern>
                </encoder>
    </appender>
    
    <!--Output to central logging -->
        <appender name="CentralLogging"
                  class="com.ctrip.freeway.appender.CentralLoggingAppender">
            <appId>100000557</appId>
            <serverIp>{$ClogIp}</serverIp>
            <serverPort>{$ClogPort}</serverPort>
        </appender>

        <appender name="bizfileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoding>UTF-8</encoding>
            <file>/opt/logs/tomcat/bizeventdispatcher.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>/opt/logs/tomcat/bizeventdispatcher.log.%d{yyyyMMddHH}</fileNamePattern>
                <maxHistory>3</maxHistory>
            </rollingPolicy>
            <encoder><!-- 必须指定，否则不会往文件输出内容 -->
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n</pattern>
            </encoder>
        </appender>

        <appender name="debugfileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <encoding>UTF-8</encoding>
            <file>/opt/logs/tomcat/debugeventdispatcher.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>/opt/logs/tomcat/debugeventdispatcher.log.%d{yyyy-MM-dd}</fileNamePattern>
                <maxHistory>3</maxHistory>
            </rollingPolicy>
            <encoder><!-- 必须指定，否则不会往文件输出内容 -->
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n</pattern>
            </encoder>
        </appender>

        <logger level="info" name="biz" additivity="false">
            <appender-ref ref="bizfileAppender"/>
        </logger>
        <root level="info">
            <appender-ref ref="debugfileAppender"/>
            <appender-ref ref="CentralLogging"/>
            <appender-ref ref="STDOUT"/>
        </root>

</configuration>