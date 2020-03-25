package alipay.manage.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alipay.manage.util.*;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import otc.api.alipay.Common;
import otc.bean.alipay.FileList;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;
import alipay.manage.util.SendUtil;
@Controller
@RequestMapping("/pay")
public class DealApi {
	private static final String ORDER = "orderid";
	@Autowired OrderAppService orderAppServiceImpl;
	@Autowired OrderService orderServiceImpl;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired QrUtil  qrUtil;
	@Autowired LogUtil logUtil;
	@Autowired NotifyUtil notifyUtil;
	static Lock lock = new ReentrantLock();
	private static final String tinyurl =  "http://tinyurl.com/api-create.php";
	Logger log = LoggerFactory.getLogger(DealApi.class);

	@RequestMapping("/alipayScan/{param:.+}")
	public String alipayScan(@PathVariable String param,HttpServletRequest request) {
		log.info("【请求交易的终端用户交易请求参数为："+param+"】");
		Map<String, Object> stringObjectMap = RSAUtils.retMapDecode(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
		if(CollUtil.isEmpty(stringObjectMap)) 
			log.info("【参数解密为空】");
		String orderId = stringObjectMap.get(ORDER).toString();
		log.info("【当前请求交易订单号为："+orderId+"】");
		DealOrderApp orderApp = orderAppServiceImpl.findOrderByOrderId(orderId);
		boolean flag = addOrder(orderApp,request);
		if(!flag)
			log.info("【订单生成有误】");
		return "pay";
	}
	private boolean addOrder(DealOrderApp orderApp, HttpServletRequest request) {
		if(!orderApp.getOrderStatus().toString().equals(Common.Order.ORDER_STATUS_DISPOSE.toString()))
			return false;
		DealOrder order = new DealOrder();
		String orderAccount = orderApp.getOrderAccount();//交易商户号
		UserInfo accountInfo = userInfoServiceImpl.findUserInfoByUserId(orderAccount);//这里有为商户配置的 供应队列属性
		String[] split = {};
		if(StrUtil.isNotBlank(accountInfo.getQueueList()))
			split  = accountInfo.getQueueList().split(",");//队列供应标识数组
		order.setAssociatedId(orderApp.getOrderId());
		order.setDealDescribe("正常交易订单");
		order.setActualAmount(orderApp.getOrderAmount());
		order.setDealAmount(orderApp.getOrderAmount());
		order.setDealFee(new BigDecimal("0"));
		order.setExternalOrderId(orderApp.getAppOrderId());
		order.setGenerationIp(HttpUtil.getClientIP(request));
		order.setOrderAccount(orderApp.getOrderAccount());
		order.setIsNotify(orderApp.getNotify());
		FileList findQr = null ;
		try {
			findQr = qrUtil.findQr(orderApp.getOrderId(), orderApp.getOrderAmount(), split);
		} catch (ParseException e) {
			log.info("【选码出现异常】");
		}
		order.setOrderQrUser(findQr.getFileholder());
		order.setOrderQr(findQr.getFileId());
		order.setOrderStatus(Common.Order.ORDER_STATUS_DISPOSE.toString());
		order.setOrderType(Common.Order.ORDER_TYPE_DEAL.toString());
		UserRate rate = userInfoServiceImpl.findUserRate(findQr.getFileholder(),Common.Deal.PRODUCT_ALIPAY_SCAN);
		order.setFeeId(rate.getId());
		boolean addOrder = orderServiceImpl.addOrder(order);
		return addOrder;
	}
}
