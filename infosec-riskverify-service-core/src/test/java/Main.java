import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.configs.utils.Utils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * Created by zhangsx on 2015/3/19.
 */
public class Main {
    public static void main(String[] args) {
        try {
            String s = "{\n" +
"\"eventPoint\": \"CP0027004\",\n" +
"\"eventBody\": {\n" +
"\"amount\": 7000,\n" +
"\"bizType\": \"90\",\n" +
"\"BookingDate\": \"/Date(1422203535917+0800)/\",\n" +
"\"CurrentAmount\": 70,\n" +
"\"IsHide\": false,\n" +
"\"ItemInfos\": [\n" +
"{\n" +
"\"DestCityId\": \"228\",\n" +
"\"DestCityName\": \"东京\",\n" +
"\"EndDate\": \"/Date(1422720000000+0800)/\",\n" +
"\"ProductName\": \"东京拼车接机服务 点击查看详情（成田机场至东京市内酒店）\",\n" +
"\"Remark\": \"自由行订单号：1210958007\",\n" +
"\"StartCityId\": \"1\",\n" +
"\"StartCityName\": \"北京\",\n" +
"\"StartDate\": \"/Date(1422720000000+0800)/\"\n" +
"}\n" +
"],\n" +
"\"Message_CreateTime\": \"2015-1-26 0:32:16\",\n" +
"\"OperateTime\": \"/Date(1422203535863+0800)/\",\n" +
"\"OrderDescription\": \"未提交\",\n" +
"\"OrderId\": 239628748,\n" +
"\"OrderStatus\": \"DIY_UNCOMMIT\",\n" +
"\"OrderType\": \"机酒\",\n" +
"\"Passengers\": [\n" +
"{\n" +
"\"AgeType\": \"ADU\",\n" +
"\"CardNo\": \"G57175296\",\n" +
"\"EName\": \"Pei/Jingyuan\",\n" +
"\"Gender\": \"M\",\n" +
"\"Mobile\": \"13718959754\",\n" +
"\"Name\": \"裴敬渊\",\n" +
"\"Nationality\": \"CN\"\n" +
"}\n" +
"],\n" +
"\"PriceAdjust\": \"2\",\n" +
"\"ProcessOper\": \"\",\n" +
"\"SourceFromCode\": \"Web\",\n" +
"\"uid\": \"JJYD\",\n" +
"\"UsedTime\": \"/Date(1422720000000+0800)/\",\n" +
"\"Version\": \"0:0\"\n" +
"}\n" +
"}\n";
            Request.Post("http://10.2.10.77:8080/ruleenginews/rule/query")
                    .body(new StringEntity(s, "UTF-8"))
                    .connectTimeout(1000).socketTimeout(5000)
                    .execute().returnContent().asString();

//
//                    byte[] response = Request.Post("http://10.2.10.77:8080/ruleenginews/rule/query")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept-Encoding", "utf-8")
//                .connectTimeout(5000)
//                .socketTimeout(10000)
//                            .body(new StringEntity(s))
//                .execute().returnContent().asBytes();
//        RiskFact riskFact = Utils.JSON.parseObject(new String(response, "utf-8"), RiskFact.class);
            String ss="";
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
