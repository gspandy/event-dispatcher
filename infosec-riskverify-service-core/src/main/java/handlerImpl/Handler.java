package handlerImpl;

import com.ctrip.infosec.common.model.RiskResult;
import java.util.Map;

/**
 * Created by zhangsx on 2015/2/3. 处理标准化好的数据
 */
public interface Handler {

    public RiskResult send(Map map);
}
