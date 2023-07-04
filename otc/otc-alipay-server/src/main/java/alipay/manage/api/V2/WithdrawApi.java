package alipay.manage.api.V2;


import alipay.manage.api.V2.vo.WithdrawRequestVO;
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
 * 代付
 */
public class WithdrawApi {

    private DealPay dealPay;
    private FindOrderAndUserInfo findOrderAndUserInfo;
    private WitPay witPay;
    private PayUtil payUtil;


    @PostMapping("/wit")
    /**
     * 创建代付订单
     * @param user
     * @return
     */
    public Result witOrder(HttpServletRequest request, @RequestBody WithdrawRequestVO withdrawRequestVO) {
    //    log.info("v2 with request:{}", JSONUtil.toJsonStr(withdrawRequestVO));
        return witPay.wit(request, false,withdrawRequestVO);
    }
}
