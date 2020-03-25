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

import alipay.manage.api.Feign.QueueServiceClienFeign;
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

@Controller
@RequestMapping("/pay")
public class DealApi {
	private static final String ORDER = "orderid";
	@Autowired OrderAppService orderAppServiceImpl;
	@Autowired OrderService orderServiceImpl;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired QrUtil  qrUtil;
	@Autowired LogUtil logUtil;
	@Autowired SendUtil sendUtil;
	@Autowired NotifyUtil notifyUtil;
	@Autowired SettingFile settingFile;
	static Lock lock = new ReentrantLock();
	private static final String tinyurl =  "http://tinyurl.com/api-create.php";
	Logger log = LoggerFactory.getLogger(DealApi.class);

	@RequestMapping("/deal")
	public String deal(HttpServletRequest request, HttpServletResponse response,String id) throws Exception {
		HashMap<String, String> decryptionParam = sendUtil.decryptionParam(request);
		String order = decryptionParam.get("orderId").toString(); //获取交易订单号
		logUtil.addLog(request,"当前下游用户发起交易，验证交易订单号:"+order+"","下游支付请求用户,账户未知");
		Future<String> execAsync2 = ThreadUtil.execAsync(() ->{
			lock.lock();
			try {
				List<DealOrder> findOrderByOrderId = orderServiceImpl.getOrderByAssociatedId(order );
				if(ObjectUtil.isNull(findOrderByOrderId)) {
//					boolean flag = orderServiceImpl.addOrder(order,trderId);
//					if(!flag) {
//						log.info("当前无可用支付渠道");
//						notifyUtil.sendYuC(order, false);
//						return "payEr";
//					}
				}
				return "";
			}finally {
				lock.unlock();
			}
		});
		if (StrUtil.isNotBlank(execAsync2.get()))
			return execAsync2.get().toString();
		String app=settingFile.getName(settingFile.APPID_FIXATION_MONEY); //非固码账户
//		if (app.equals(trderId.getApp_id()))
//			return "toFixationPay";
		return "pay";
	}


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
    @RequestMapping("/addOrder")
	@ResponseBody
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

	/**
	 * <p>获取交易订单</p>
	 * @param orderNo			订单号
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getOrderGatheringCode")
	@ResponseBody
	public Result getOrderGatheringCode(String orderNo) throws Exception {
		HashMap<String,String> decryptionParam = sendUtil.decryptionParam(orderNo);
		String orderId = decryptionParam.get("orderId");//对加密数据进行解密
		DealOrder order = CollUtil.getFirst(orderServiceImpl.getOrderByAssociatedId(orderId));
		//TODO 一下为一键唤醒代码   打开注释即可
	/*	String orderQr = order.getOrderQr();
		QrCode qr = QrCodeServiceImpl.findQrByNo(orderQr);
		if(StrUtil.isNotBlank(qr.getPid())) {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("url", "alipays://platformapi/startapp?appId=09999988&actionType=toAccount&goBack=NO&amount="+order.getDealAmount()+"&userId="+qr.getPid()+"&memo=备注"));
			String sendHttpsGet = SendUtil.sendHttpsGet(tinyurl, params);
			order.setTinyurl(sendHttpsGet);
			order.setPid(qr.getPid());
		}*/
	   return Result.buildSuccessMessageCode("订单获取成功", order, 200);
	}
	/**
	 * <p>获取交易订单</p>
	 * @param orderNo			订单号
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getOrderGatheringCode1")
	@ResponseBody
	public Result getOrderGatheringCode1(String orderNo) throws Exception {
		//对加密数据进行解密
		DealOrder order = orderServiceImpl.findOrderByOrderId(orderNo);
		return Result.buildSuccessMessageCode("订单获取成功", order, 200);
	}
	/**
	 * <p>补单交易接口</p>
	 * @Param orderId
	 */
  @PostMapping("/replen")
  @ResponseBody
  public Result replen(String orderId){
     log.info("下游发起补单,当前补单订单号");
	  /**
	   * <p>1，获取当前补单的交易订单</p>
	   * <p>2.获取当前下游的交易用户</p>
	   * <p>3.将订单定为超时</p>
	   * <p>4.向下游发送通知</p>
	   */
	  if(StrUtil.isBlank(orderId))
	  	return Result.buildFailMessage("参数为空");
	  DealOrder order=orderServiceImpl.findOrderByOrderId(orderId);
	  if(ObjectUtil.isNotNull(order))
	  	return Result.buildFailMessage("查无此单");
	  boolean flag=orderServiceImpl.updataOrderStatusByOrderId(orderId,"3");//交易订单 未收到回调
	  if (flag)
	  	notifyUtil.sendMsg(orderId,true);
	  return Result.buildSuccess();
  }


}
