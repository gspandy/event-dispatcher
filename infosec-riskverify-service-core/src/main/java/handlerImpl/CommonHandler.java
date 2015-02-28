package handlerImpl;
import biz.RiskVerifyBiz;
import com.ctrip.infosec.common.model.RiskResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by zhangsx on 2015/2/3.
 */
public class CommonHandler implements Handler {
    @Autowired
    private RiskVerifyBiz biz;

    @Override
    public RiskResult send(Map map) {
        return biz.exe(map);
    }
}
