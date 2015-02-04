import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.riskverify.biz.RiskVerifyBiz;
import com.ctrip.infosec.riskverify.biz.rabbitmq.RabbitMqSender;
import com.ctrip.infosec.sars.monitor.util.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhangsx on 2015/1/7.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
public class RiskVerifyBizTest {
    @Autowired
    private RiskVerifyBiz biz;
    private RiskFact fact = null;
    private RiskFact expectedObj = null;
    private final String msg = "{\n" +
            "  \"eventPoint\": \"EP1001\",\n" +
            "  \"appId\": \"com.ctrip.rule.test\",\n" +
            "  \"eventBody\": {\n" +
            "    \"OrderID\": 76381293721903,\n" +
            "    \"OrderType\": 18,\n" +
            "    \"OrderAmount\": 1000,\n" +
            "\n" +
            "    \"Uid\": \"\",\n" +
            "    \"UserIP\": \"\",\n" +
            "    \"MerchantOrderID\": \"\",\n" +
            "    \"results\": [\n" +
            "      {}\n" +
            "    ],\n" +
            "    \"finalResult\": [\n" +
            "      {}\n" +
            "    ],\n" +
            "    \"ext\": [\n" +
            "      {}\n" +
            "    ],\n" +
            "    \"TieYouOrderInfos\": [\n" +
            "      {\n" +
            "       \n" +
            "      }\n" +
            "    ],\n" +
            "    \"PaymentInfos\": [\n" +
            "      {\n" +
            "        \"Amount\": 0,\n" +
            "        \"CardInfoID\": 88892,\n" +
            "        \"PrepayType\": \"CCARD\",\n" +
            "        \"RefNo\": 23812903701237,\n" +
            "        \"CreditCardInfo\": {\n" +
            "          \"BankOfCardIssue\": \"\",\n" +
            "          \"BillingAddress\": \"\",\n" +
            "          \"CardBin\": \"\",\n" +
            "          \"CardHolder\": \"\",\n" +
            "          \"CardInfoID\": 3213123,\n" +
            "          \"CCardLastNoCode\": \"8903908130\",\n" +
            "          \"CCardNoCode\": \"xw3e3r345435\",\n" +
            "          \"CCardPreNoCode\": \"xsxsxsx\",\n" +
            "          \"CreditCardType\": 1,\n" +
            "          \"CValidityCode\": \"xsxsdsd\",\n" +
            "          \"InfoID\": 2321312,\n" +
            "          \"IsForigenCard\": \"T\",\n" +
            "          \"Nationality\": \"CN\",\n" +
            "          \"Nationalityofisuue\": \"PRC\",\n" +
            "          \"StateName\": \"BJ\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @Before
    public void setFact() {
        fact = Utils.JSON.parseObject(msg, RiskFact.class);

    }


    @Test
    @Ignore
    public void bizTest() {

//        RiskResult resp = biz.exe(fact, "TEST");
//        System.out.println(Utils.JSON.toJSONString(resp));
//
//        System.out.println(((String) (((Map) resp.getResults().get("tie_you_1")).get("desc"))));
//        Assert.assertArrayEquals("信用卡支付铁友订单，金额>=100".getBytes(), ((String) (((Map) resp.getResults().get("tie_you_1")).get("desc"))).getBytes());


    }

    @Test
    @Ignore
    /**
     * rabbitmq 发送测试
     */
    public void mqSendTest() {
        RabbitMqSender sender = null;
        try {
            sender = RabbitMqSender.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean b = false;
        try {
            sender.send("test0");
        } catch (Exception e) {
            b = true;
        }
        Assert.assertFalse(b);
    }

    @Test
    /**
     * rabbitmq 接收测试
     */
    public void mqHandleTest() {

    }
}
