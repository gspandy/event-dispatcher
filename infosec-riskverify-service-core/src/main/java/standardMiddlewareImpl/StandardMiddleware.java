package standardMiddlewareImpl;

import com.ctrip.infosec.configs.event.Channel;

/**
 * Created by zhangsx on 2015/2/3. 标准化中间件 对不标准数据进行标准化
 */
public interface StandardMiddleware {

    /**
     * 添加需要标准化的管道
     */
    public void assembleAndSend(Channel channel, String eventPoint, byte[] body);
}
