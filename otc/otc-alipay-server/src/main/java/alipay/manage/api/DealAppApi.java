package alipay.manage.api;

import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.deal.DealPay;
import alipay.manage.api.deal.FindOrderAndUserInfo;
import alipay.manage.api.deal.PayUtil;
import alipay.manage.api.deal.WitPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>商户请求交易接口</p>
 *
 * @author hx08
 */
@RestController
@RequestMapping("/deal")
public class DealAppApi extends PayOrderService {
    Logger log = LoggerFactory.getLogger(DealAppApi.class);
    @Autowired
    private DealPay dealPay;
    @Autowired
    private FindOrderAndUserInfo findOrderAndUserInfo;
    @Autowired
    private WitPay witPay;
    @Autowired
    private PayUtil payUtil;

    @RequestMapping("/findFund")
    public Result findFund(HttpServletRequest request) {
        log.info("【查询资金账户详情，请求参数为：" + request.getQueryString() + "】");
        log.info("【访问URL：" + request.getRequestURL() + "】");
        try {
            Result fund = findOrderAndUserInfo.findFund(request);
            log.info("【处理结果为：" + fund.toString() + "】");
            return fund;
        } catch (Exception e) {
            log.info("【处理结果异常】");
            return Result.buildFailMessage("查询失败");
        }
    }

    @RequestMapping("/findOrder")
    public Result findOrder(HttpServletRequest request) {
        log.info("【查询订单详情，请求参数为：" + request.getQueryString() + "】");
        log.info("【访问URL：" + request.getRequestURL() + "】");
        try {
            Result order = findOrderAndUserInfo.findOrder(request);
			log.info("【处理结果为：" + order.toString() + "】");
			return order;
		} catch (Exception e) {
			log.info("【处理结果异常】");
			return Result.buildFailMessage("查询失败");
		}
	}

	@RequestMapping("/findOrderSum")
	public Result findOrderSum(HttpServletRequest request) {
        log.info("【查询订单汇总详情，请求参数为：" + request.getQueryString() + "】");
        log.info("【访问URL：" + request.getRequestURL() + "】");
        try {
            Result order = findOrderAndUserInfo.findOrderSum(request);
            log.info("【处理结果为：" + order.toString() + "】");
            return order;
        } catch (Exception e) {
            log.info("【处理结果异常】");
            return Result.buildFailMessage("查询失败");
        }
    }

    @PostMapping("/pay")
    public Result dealAppPay(HttpServletRequest request) {
        return dealPay.deal(request);
    }

    @PostMapping("/wit")
    public Result witOrder(HttpServletRequest request) {
        return witPay.wit(request, false);
    }

    @PostMapping("/witCheckAmount")
    public Result witClickAmount(HttpServletRequest request) {
        return witPay.wit(request, true);
    }


    @PostMapping("/help/decode")
    public Result helpDecode(HttpServletRequest request) {
        return payUtil.decode(request);
    }

}
