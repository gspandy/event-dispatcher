package standardMiddlewareImpl;

import java.util.Map;

/**
 * Created by zhangsx on 2015/2/3.
 * 标准化中间件 对不标准数据进行标准化
 */
public interface StandardMiddleware {
    /**
     * 添加需要标准化的管道
     */
//    void addSourcePipeline(List<Pipeline> source);
//    /**
//     * 添加标准化好的数据的目标管道
//     */
//    void addTargetPipeline(List<Pipeline> target);
//    /**
//     * 开始数据标准化工作
//     */
//    void work();
    public void assembleAndSend(Map map);
}
