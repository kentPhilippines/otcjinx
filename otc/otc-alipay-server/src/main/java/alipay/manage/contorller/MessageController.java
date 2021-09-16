package alipay.manage.contorller;

import alipay.manage.api.channel.amount.BalanceInfo;
import alipay.manage.api.channel.util.QueryBalanceTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author water
 */
@Controller
@RequestMapping("/balance")
public class MessageController {

    @GetMapping("/getMerchantBalance")
    @ResponseBody
    public Result getUserAccountInfo(HttpServletRequest request) {
        List<BalanceInfo> balance = QueryBalanceTool.findBalance();
        return Result.buildSuccessResult(balance);
    }

}
