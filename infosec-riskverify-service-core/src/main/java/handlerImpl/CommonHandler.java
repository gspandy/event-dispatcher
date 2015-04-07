package handlerImpl;

import biz.RiskVerifyBiz;
import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.event.Channel;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by zhangsx on 2015/2/3.
 */
public class CommonHandler implements Handler {
    
    @Autowired
    private RiskVerifyBiz biz;
    
    @Override
    public RiskResult send(Channel channel, RiskFact fact) {
        return biz.exe(channel, fact);
    }
}
