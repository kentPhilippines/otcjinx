package deal.manage.contorller;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import deal.config.feign.ConfigServiceClient;
import deal.manage.bean.BankList;
import deal.manage.bean.UserInfo;
import deal.manage.service.BankListService;
import deal.manage.service.OrderService;
import deal.manage.service.RechargeService;
import deal.manage.service.UserFundService;
import deal.manage.service.UserInfoService;
import deal.manage.service.WithdrawService;
import deal.manage.util.LogUtil;
import deal.manage.util.SessionUtil;
import otc.api.dealpay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Recharge;
import otc.result.Result;
import otc.util.number.Number;

@Controller
@RequestMapping("/recharge")
public class RechargeController {
	Logger log = LoggerFactory.getLogger(RechargeController.class);
	@Autowired UserInfoService accountServiceImpl;
	@Autowired SessionUtil sessionUtil;
	@Autowired LogUtil logUtil;
	@Autowired BankListService bankCardSvc;
    @Autowired OrderService orderServiceImpl;
    @Autowired UserFundService userFundServiceImpl;
    @Autowired UserInfoService userInfoServiceImpl;
    @Autowired WithdrawService withdrawServiceImpl;
    @Autowired RechargeService rechargeServiceImpl;
	@Autowired ConfigServiceClient configServiceClientImpl;
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
        log.info("【参数信息："+param.toString()+"】");
        if(ObjectUtil.isNull(user))
            return Result.buildFailMessage("当前用户未登陆");
        if(ObjectUtil.isNull(param.getAmount() )
        		|| StrUtil.isBlank(param.getDepositor()) 
        		|| StrUtil.isBlank(param.getPhone())
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
        Recharge createRechrage = createRechrage(param);
        if(ObjectUtil.isNull(createRechrage))
        	return Result.buildFailMessage("充值订单生成失败");
        String url = "orderId="+createRechrage.getOrderId()+"&type=2";//异常重要
        return Result.buildSuccessResult(configServiceClientImpl.getConfig(ConfigFile.DEAL, ConfigFile.Deal.RECHARGE_URL).getResult().toString()+url);
    }
    Recharge createRechrage(Recharge param){
    	List<BankList> bankcardList = bankCardSvc.findSystemBank();
    	if(CollUtil.isEmpty(bankcardList))
    		return null;
    	Collections.shuffle(bankcardList);
    	Collections.shuffle(bankcardList);
    	Collections.shuffle(bankcardList);
    	BankList bank = CollUtil.getFirst(bankcardList);
    	Recharge  order = new Recharge();
    	order.setActualAmount(param.getAmount());
    	order.setOrderId(Number.getRechargeCa());
    	order.setAmount(param.getAmount());
    	order.setRechargeType(Common.Order.Recharge.WIT_BK);
    	order.setFee(new BigDecimal("0"));
    	order.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
    	order.setUserId(param.getUserId());
    	order.setRetain1(param.getRetain1());
    	order.setDepositor(param.getDepositor());
    	order.setPhone(param.getPhone());
    	order.setChargeBankcard(bank.getBankcardId());
	   boolean flag =  rechargeServiceImpl.addOrder(order);
	   if(flag)
		   return order;
	   return null;
    }
}
