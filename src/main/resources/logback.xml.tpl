<?xml version="1.0" encoding="UTF-8"?>
<configuration>
<!--Output to central logging -->
<appender name="CentralLogging"
class="com.ctrip.freeway.appender.CentralLoggingAppender">
<appId>100000557</appId>
<serverIp>{$ClogIp}</serverIp>
<serverPort>{$ClogPort}</serverPort>
</appender>

<root level="INFO">
<appender-ref ref="CentralLogging" />
</root>

</configuration>