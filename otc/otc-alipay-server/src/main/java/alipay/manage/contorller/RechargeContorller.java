package alipay.manage.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.result.Result;


@Controller
@RequestMapping("/recharge")
public class RechargeContorller {
    Logger log = LoggerFactory.getLogger(RechargeContorller.class);

    /**
     * <p>获取可用的充值渠道</p>
     * <p>这里的渠道就是自营产品</p>
     * @return
     */
    @RequestMapping("/findEnabledPayType")
    @ResponseBody
    public Result findEnabledPayType() {
//        List<Paytype> list = payTypeServiceImpl.findPayTypeAll();
        return Result.buildSuccessMessage(null);
    }
}
