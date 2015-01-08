package com.ctrip.infosec.riskverify.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Executor;

/**
 * Created by zhangsx on 2015/1/8.
 */
public class RiskVerifyAdvice implements MethodInterceptor {
    private final Log logger = LogFactory.getLog(RiskVerifyAdvice.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String serviceName = invocation.getThis().getClass().getSimpleName();
        String operationName = invocation.getMethod().getName();

        StopWatch clock = new StopWatch();
        clock.start();

        try {
            return invocation.proceed();
        } catch (Exception ex) {
            logger.warn("["+serviceName+":"+operationName+"]"+"throw "+ex);
            throw ex;
        } catch (Throwable t) {
            logger.warn("["+serviceName+":"+operationName+"]"+"throw "+t);
            throw t;
        } finally {
            clock.stop();
            long handingtime = clock.getNanoTime();
            System.out.println("[" + serviceName+":"+operationName+"]"+"服务耗时："+handingtime+"ms");
        }

    }
}
