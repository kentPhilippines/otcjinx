package alipay.manage.api.V2;


import alipay.manage.api.V2.vo.DepositRequestVO;
import alipay.manage.api.deal.DealPay;
import alipay.manage.api.deal.FindOrderAndUserInfo;
import alipay.manage.api.deal.PayUtil;
import alipay.manage.api.deal.WitPay;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/v2/deal")
@AllArgsConstructor
/**
 * 代收
 */
public class DepositApi {

    private DealPay dealPay;
    private FindOrderAndUserInfo findOrderAndUserInfo;
    private WitPay witPay;
    private PayUtil payUtil;

    /**
     * 创建代收订单
     * @param depositRequestVO
     * @return
     */
    @PostMapping("/pay")
    public Result dealAppPay(HttpServletRequest request, @RequestBody DepositRequestVO depositRequestVO) {
        log.info("v2 depsoit request:{}", JSONUtil.toJsonStr(depositRequestVO));

        return dealPay.deal(request,depositRequestVO);
    }
}
