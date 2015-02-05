import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.biz.rabbitmq.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by zhangsx on 2015/1/7.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
public class RiskVerifyBizTest {
    @Autowired
    private RabbitMqSender sender;
    @Autowired
    private RiskVerifyBiz biz;

    @Test
    public void senderTest(){
        sender.init();
        System.out.println("***");
        sender.send("hello");
    }

    @Test
    public void bizTest(){
        String msg = "{\n" +
                "\"eventPoint\": \"CP0011004\",\n" +
                "\"eventBody\": {\n" +
                "\"actualAmount\": 650,\n" +
                "\"amount\": 6500,\n" +
                "\"bizType\": \"11\",\n" +
                "\"bookingDate\": \"/Date(1422385584947+0800)/\",\n" +
                "\"contactInfo\": \"{\\\"Name\\\":\\\"携程客户\\\",\\\"Tel\\\":\\\"\\\",\\\"Mobile\\\":\\\"013980868606\\\",\\\"Email\\\":\\\"\\\",\\\"Fax\\\":\\\"\\\",\\\"ConfirmType\\\":\\\"CSM\\\"}\",\n" +
                "\"currency\": \"RMB\",\n" +
                "\"firstDepartureTime\": \"/Date(1422577500000+0800)/\",\n" +
                "\"isHide\": false,\n" +
                "\"isPartial\": \"\",\n" +
                "\"itemInfos\": [\n" +
                "{\n" +
                "\"ArrivalTime\": \"/Date(1422586200000+0800)/\",\n" +
                "\"Description\": \"商旅三方协议客户不适用，限邮寄行程单及成都市区送取、机场取票。\",\n" +
                "\"FlightNo\": \"EU2217\",\n" +
                "\"FlightWay\": \"S\",\n" +
                "\"FromAddress\": \"CTU\",\n" +
                "\"FromCityId\": \"28\",\n" +
                "\"FromCityName\": \"成都\",\n" +
                "\"IsSurface\": \"F\",\n" +
                "\"OrderCategory\": \"Flight\",\n" +
                "\"Price\": 570,\n" +
                "\"SubClass\": \"V\",\n" +
                "\"TakeOffTime\": \"/Date(1422577500000+0800)/\",\n" +
                "\"ToAddress\": \"SZX\",\n" +
                "\"ToCityId\": \"30\",\n" +
                "\"ToCityName\": \"深圳\"\n" +
                "}\n" +
                "],\n" +
                "\"message_CreateTime\": \"2015-1-28 3:06:25\",\n" +
                "\"operateTime\": \"/Date(1422385584973+0800)/\",\n" +
                "\"operators\": \"\",\n" +
                "\"OrderDescription\": \"未提交\",\n" +
                "\"orderId\": 1212830376,\n" +
                "\"orderStatus\": \"FLIGHT_UNCOMMIT\",\n" +
                "\"orderType\": \"国内\",\n" +
                "\"passengers\": [\n" +
                "{\n" +
                "\"AgeType\": \"ADU\",\n" +
                "\"BirthDate\": \"1979-9-19 0:00:00\",\n" +
                "\"CardNo\": \"510221197909190614\",\n" +
                "\"CardType\": \"1\",\n" +
                "\"Gender\": \"M\",\n" +
                "\"Name\": \"朱诚\"\n" +
                "}\n" +
                "],\n" +
                "\"remarks\": \"\",\n" +
                "\"serverFrom\": \"client/android/sanxing\",\n" +
                "\"sourceFromCode\": \"APP\",\n" +
                "\"specialPriceType\": \"SR\",\n" +
                "\"ticketStatus\": \"A\",\n" +
                "\"uid\": \"_zx514906000183\",\n" +
                "\"version\": \"0:0\"\n" +
                "}\n" +
                "}\n";
        Map body = Utils.JSON.parseObject(msg,Map.class);
        Map map = ImmutableMap.of("FACT", "Test", "CP", "CP0011004", "body", body);
        biz.exe(map);
    }
}
