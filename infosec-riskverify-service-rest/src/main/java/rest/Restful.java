package rest;

import com.ctrip.infosec.common.model.RiskFact;
import com.ctrip.infosec.common.model.RiskResult;
import com.ctrip.infosec.configs.event.Channel;
import static com.ctrip.infosec.configs.utils.Utils.JSON;
import com.ctrip.infosec.sars.monitor.util.Utils;
import handlerImpl.Handler;
import manager.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangsx on 2015/2/3.
 */
@Controller
public class Restful implements Receiver {

    private static final Logger logger = LoggerFactory.getLogger("biz");
    @Autowired
    private Handler handler;

    /**
     * 风控审核（针对外部PD、只返回finalResult）
     *
     * @param fact
     * @return
     */
    @RequestMapping(value = "/riskverify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RiskResult> riskverify(@RequestBody RiskFact fact) {
        logger.info("[verify] fact: " + Utils.JSON.toJSONString(fact));
        RiskResult result = handler.verify(Channel.REST, fact);
        return new ResponseEntity<RiskResult>(result, HttpStatus.OK);
    }

    /**
     * 风控审核（针对外部PD、只返回finalResult、调用时不需要设置Header）
     *
     * @param factTxt
     * @return
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RiskResult> verify(@RequestBody String factTxt) {
        logger.info("[verify] fact: " + factTxt);
        RiskFact fact = JSON.parseObject(factTxt, RiskFact.class);
        RiskResult result = handler.verify(Channel.REST, fact);
        return new ResponseEntity<RiskResult>(result, HttpStatus.OK);
    }

    /**
     * 执行规则验证（针对支付风控、返回finalResult以及results、调用时不需要设置Header）
     *
     * @param factTxt
     * @return
     */
    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RiskFact> execute(@RequestBody String factTxt) {
        logger.info("[execute] fact: " + factTxt);
        RiskFact fact = JSON.parseObject(factTxt, RiskFact.class);
        fact = handler.execute(Channel.REST, fact);
        fact.eventBody.clear();
        fact.ext.clear();
        return new ResponseEntity<RiskFact>(fact, HttpStatus.OK);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public @ResponseBody
    String checkHealth() {
        return "hello!";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

}
