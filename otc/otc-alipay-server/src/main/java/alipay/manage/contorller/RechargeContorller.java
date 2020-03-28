package alipay.manage.contorller;

import alipay.manage.api.channel.amount.AmountChannel;
import alipay.manage.api.config.FactoryForStrategy;
import alipay.manage.bean.Product;
import alipay.manage.bean.Recharge;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.OrderService;
import alipay.manage.service.ProductService;
import alipay.manage.util.LogUtil;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import otc.api.alipay.Common;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/recharge")
public class RechargeContorller {
    Logger log = LoggerFactory.getLogger(RechargeContorller.class);
    @Autowired SessionUtil sessionUtil;
    @Autowired LogUtil logUtil;
    @Autowired ProductService productService;
    @Autowired FactoryForStrategy factoryForStrategy;
    @Autowired OrderService orderServiceImpl;
    private static final String MY_RECHARGE = "MyChannelRecharge";
    /**
     * <p>获取可用的充值渠道</p>
     * <p>这里的渠道就是自营产品</p>
     * @return
     */
    @RequestMapping("/findEnabledPayType")
    @ResponseBody
    public Result findEnabledPayType() {
       List<Product> list = productService.findAllProduct();
        return Result.buildSuccessResult("发送成功",list);
    }

    /**
     * 充值生成订单
     * @param param
     * @param request
     * @return
     */
    @PostMapping("/generateRechargeOrder")
    @ResponseBody
    public Result generateRechargeOrder(Recharge param, HttpServletRequest request) {
        UserInfo user = sessionUtil.getUser(request);
        if(ObjectUtil.isNull(user))
            return Result.buildFailMessage("当前用户未登陆");
        if(ObjectUtil.isNull(param.getAmount() )
        		|| StrUtil.isBlank(param.getDepositor()) 
        		|| StrUtil.isBlank(param.getPhone())
        		|| StrUtil.isBlank(param.getSync_url())
        		)
        		return Result.buildFailMessage("关键信息为空");
        param.setUserId(user.getUserId());
        String clientIP = HttpUtil.getClientIP(request);
		if(StrUtil.isBlank(clientIP))
			return Result.buildFailMessage("当前使用代理服务器 或是操作ip识别出错，不允许操作");
		param.setRetain1(clientIP);
        String msg = "码商发起充值操作,当前充值参数：充值金额："+param.getAmount()+"，充值人姓名："+ param.getDepositor()+
                "，关联码商账号："+user.getUserId()+"，充值手机号："+ param.getPhone();
        boolean addLog = logUtil.addLog(request, msg, user.getUserId());
        log.info("获取addLog"+addLog);
        Result createRechrage = createRechrage(param);
        if(!createRechrage.isSuccess())
        	return Result.buildFailMessage("充值订单生成失败");
		Result recharge = null;
		try {
			recharge = factoryForStrategy.getAmountChannel(MY_RECHARGE).recharge(param);
		} catch (Exception e) {
			 return Result.buildFailMessage("暂无充值渠道");
		}
        if(recharge.isSuccess()) 
        	return Result.buildSuccessResult(recharge.getResult());
        return Result.buildFailMessage("暂无充值渠道");
    }
   Result createRechrage(Recharge param){
	   Recharge  order = new Recharge();
	   order.setActualAmount(param.getAmount());
	   order.setAmount(param.getAmount());
	   order.setRechargeType(param.getRechargeType());
	   order.setFee(new BigDecimal("0"));
	   order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
	   order.setNotfiy("127.0.0.1:3212/otc/rechaege-notfiy");
	   order.setUserId(param.getUserId());
	   order.setRetain1(param.getRetain1());
	   boolean flag =  orderServiceImpl.addRechargeOrder(order);
	   if(flag)
		   return Result.buildSuccess();
	   return Result.buildFail();
    }
    
    
    
}
