package alipay.manage.api;

import alipay.manage.util.CheckUtils;
import alipay.manage.util.LogUtil;
import alipay.manage.util.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/finance")
public class FinanceApi {
    Logger logger = LoggerFactory.getLogger(FinanceApi.class);

    @Autowired
    CheckUtils checkUtils;

    @Autowired
    OrderUtil orderUtil;

    @Autowired
    LogUtil logUtil;

    /**
     * 财务审核商户提现记录状态
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/check/{param:.+}")
    public Result checkWithdrawal(@PathVariable String param, HttpServletRequest request) {
        logger.info("【==============开始进入财务审核商户提现方法===============】");
        logger.info("【财务审核商户的提现记录】后台传过来的参数：{}", param);
        //平台内部私钥解密后台传过来的参数
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
        logger.info("【财务审核商户的提现记录】解密后台参数为：{}", paramMap);
        //验证参数非空
        boolean flag = checkUtils.verifyParamNull(paramMap);
        if (!flag) {
            return Result.buildFailMessage("必传参数为空");
        }
        if (Common.Order.Wit.ORDER_STATUS_SU.equals(paramMap.get("orderStatus"))) {//成功
            logUtil.addLog(request, "当前后台审核商户提现订单操作：" + paramMap.get("orderId") + "，：" + paramMap.get("userId") + "，操作人：" + paramMap.get("approval").toString() + ", 审核意见：" + paramMap.get("comment").toString(), paramMap.get("approval").toString());
            return orderUtil.withrawOrderSu(paramMap.get("orderId").toString(), paramMap.get("approval").toString(), paramMap.get("comment").toString());
        }
        if (Common.Order.Wit.ORDER_STATUS_ER.equals(paramMap.get("orderStatus"))) {//失败
            logUtil.addLog(request, "当前后台审核商户提现订单操作：" + paramMap.get("orderId") + "，：" + paramMap.get("userId") + "，操作人：" + paramMap.get("approval").toString() +  ", 审核意见：" + paramMap.get("comment").toString(), paramMap.get("approval").toString());
            return orderUtil.withrawOrderEr(paramMap.get("orderId").toString(), paramMap.get("approval").toString(), paramMap.get("comment").toString(), paramMap.get("ip").toString());
        }
        return null;
    }


}
