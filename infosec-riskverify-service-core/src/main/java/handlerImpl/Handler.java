package handlerImpl;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.event.Channel;

/**
 * Created by zhangsx on 2015/2/3. 处理标准化好的数据
 */
public interface Handler {

    public RiskResult send(Channel channel, RiskFact fact);
}
