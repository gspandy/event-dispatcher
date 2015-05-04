package handlerImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.event.Channel;

/**
 * Created by zhangsx on 2015/2/3. 处理标准化好的数据
 */
public interface Handler {

    /**
     * 风控审核（针对外部PD、只返回finalResult）
     *
     * @param channel
     * @param fact
     * @return
     */
    public RiskResult verify(Channel channel, RiskFact fact);

    /**
     * 执行规则验证（针对支付风控、返回finalResult以及results）
     *
     * @param channel
     * @param fact
     * @return
     */
    public RiskFact execute(Channel channel, RiskFact fact);
}
