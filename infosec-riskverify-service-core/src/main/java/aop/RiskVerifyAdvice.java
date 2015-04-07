package aop;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.configs.event.Channel;
import com.ctrip.infosec.configs.event.monitor.EventCounterRepository;
import com.ctrip.infosec.sars.monitor.counters.CounterRepository;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by zhangsx on 2015/1/8.
 */
public class RiskVerifyAdvice implements MethodInterceptor {

    private final Log logger = LogFactory.getLog(RiskVerifyAdvice.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Channel channel = (Channel) invocation.getArguments()[0];
        RiskFact fact = (RiskFact) invocation.getArguments()[1];
        String eventPoint = fact.getEventPoint();

        // invoke
        boolean fault = false;
        StopWatch clock = new StopWatch();
        clock.start();
        try {
            return invocation.proceed();
        } catch (Exception ex) {
            fault = true;
            throw ex;
        } catch (Throwable t) {
            fault = true;
            throw t;
        } finally {
            clock.stop();
            long handlingTime = clock.getTime();
            CounterRepository.increaseCounter(eventPoint + "." + channel, handlingTime, fault);
            CounterRepository.increaseCounter(channel.toString(), handlingTime, fault);
            EventCounterRepository.increaseCounter(eventPoint, channel);
        }
    }
}
