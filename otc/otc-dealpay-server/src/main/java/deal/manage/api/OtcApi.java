package deal.manage.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import deal.manage.bean.BankList;
import deal.manage.bean.DealOrder;
import deal.manage.bean.Withdraw;
import deal.manage.bean.util.OTC365Bean;
import deal.manage.bean.util.OTC365Bean.orderPayinfo;
import deal.manage.bean.util.OTCLimitBean;
import deal.manage.service.*;
import deal.manage.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.bean.dealpay.Recharge;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/third")
public class OtcApi {
	Logger log = LoggerFactory.getLogger(OtcApi.class);
	@Autowired
	OrderService orderSerciceImpl;
	@Autowired
	WithdrawService withdrawServiceImpl;
	@Autowired
	BankListService bankListServiceImpl;
	@Autowired
	CardBankOrderUtil cardBankOrderUtil;
	@Autowired
	IsDealIpUtil isDealIpUtil;
	@Autowired OrderStatusService orderStatusServiceImpl;
	@Autowired RechargeService rechargeServiceImpl;
	@Autowired LogUtil logUtil;
	@Autowired EnterOrderUtil enterOrderUtil;
	@Autowired SessionUtil sessionUtil;
	private static final String TYPE_CARD = "3";//卡商自己查看出款详情
	private static final String TYPE_QR = "1";//码商查看订单
	private static final String TYPE_CARD_RE = "2";//卡商查看自己的充值订单
	
	
	/**
	 * <p>码商购买虚拟货币接口</p>
	 * <p>获取订单数据</p>
	 * <p>获取获取码商购买虚拟币交易订单,通过订单号</p>
	 * @return
	 */
	@GetMapping("/merchantBuyApi/findOrder")
	@ResponseBody
	public Result agentOpenAnAccount(String orderId,String type) {
		log.info("otc365页面打开");
		log.info(orderId);
		log.info("当前查询订单类型为:"+type);
		long dataSu = 0 ;
		OTC365Bean bean = new OTC365Bean();
		DealOrder first = new DealOrder();
		orderPayinfo orderPayinfo = null;
	//	String qrUrl = settingFile.getName(SettingFile.QR_APP_URL);
		if(TYPE_QR.equals(type)) {//码商查看自己的充值订单
			first = orderSerciceImpl.findOrderByOrderId(orderId);
			bean.setSync_url(first.getBack());
		}else if (TYPE_CARD.equals(type)) {//卡商查看出款详情
			first = orderSerciceImpl.findOrderByOrderId(orderId);
		}else if (TYPE_CARD_RE.equals(type)) {//卡商充值充值订单
			Recharge rechargeOrder = rechargeServiceImpl.findOrderId(orderId);
			BigDecimal amount = rechargeOrder.getAmount();//卡商充值金额
			orderPayinfo = bean.new orderPayinfo();
			bean.setOrderNumber(rechargeOrder.getOrderId());
			String banNo = rechargeOrder.getChargeBankcard();//【卡商自己充值时候的银行卡号】
			bean.setOrderSumPrice(amount.toString());//设置交易订单金额
			Date afterDate = new Date(rechargeOrder.getCreateTime() .getTime() + 900000);
			 try {
				 dataSu = dataSu(rechargeOrder.getCreateTime(),afterDate);
			} catch (ParseException e) {
				log.info("时间转换失败");
				dataSu = 900;
			}
			 bean.setOrderAmount(amount.divide(new BigDecimal(7),2).toString());
			bean.setValidTime(""+dataSu);
			BankList bank = bankListServiceImpl.findBankInfoNo(banNo);
			orderPayinfo.setBankName(bank.getOpenAccountBank());
			orderPayinfo.setRegisterAddress(bank.getQrcodeNote());
			orderPayinfo.setRealName(bank.getAccountHolder());
			orderPayinfo.setPayNo(bank.getBankcardAccount());
			orderPayinfo.setBank_id(bank.getBankcardId());
			orderPayinfo.setRegisterAddress(bank.getQrcodeNote());
			bean.setOrderPayinfo(orderPayinfo);
			return Result.buildSuccessResult(bean);
		}
		orderPayinfo = bean.new orderPayinfo();
		bean.setOrderNumber(first.getOrderId());
		bean.setOrderAmount(first.getDealAmount().divide(new BigDecimal(7),2).toString());
		String bankNo = first.getOrderQr();//银行卡账号
		bean.setOrderSumPrice(first.getDealAmount().toString());//设置交易订单金额
		try {
			 dataSu = dataSu(first.getCreateTime(),first.getUsefulTime());
		} catch (ParseException e) {
			log.info("时间转换失败");
			dataSu = 900;
		}
		log.info("超时时间:"+first);
		log.info("超时时间:"+dataSu);
		bean.setValidTime(""+dataSu);
		BankList bank = bankListServiceImpl.findBankInfoNo(bankNo);
		if(ObjectUtil.isNull(bank)) {
			bank = new BankList();
			Withdraw withdraw = withdrawServiceImpl.findOrderId(first.getAssociatedId());
			String accname = withdraw.getAccname();
			bank.setAccountHolder(accname);
			bank.setOpenAccountBank(withdraw.getBankName());
			bank.setBankcardAccount(withdraw.getBankNo());
		}
		orderPayinfo.setBankName(bank.getOpenAccountBank());
		orderPayinfo.setRegisterAddress(bank.getQrcodeNote());
		orderPayinfo.setRealName(bank.getAccountHolder());
		orderPayinfo.setPayNo(bank.getBankcardAccount());
		orderPayinfo.setBank_id(bank.getBankcardId());
		orderPayinfo.setRegisterAddress(bank.getQrcodeNote());
		log.info(bank.toString());
		bean.setOrderPayinfo(orderPayinfo);
		return Result.buildSuccessResult(bean );
	}
	
	
	@GetMapping("/limit")
	@ResponseBody
	public Result limit() {
		List<OTCLimitBean> list = new ArrayList<OTCLimitBean>();
		OTCLimitBean otcLimitBean = new OTCLimitBean();
		otcLimitBean.setMax("1000.00");
		otcLimitBean.setMin("0.00");
		otcLimitBean.setPay_way("2");
		OTCLimitBean otcLimitBean1 = new OTCLimitBean();
		otcLimitBean1.setMax("1000.00");
		otcLimitBean1.setMin("0.00");
		otcLimitBean1.setPay_way("3");
		OTCLimitBean otcLimitBean2 = new OTCLimitBean();
		otcLimitBean2.setMax("200000.00");
		otcLimitBean2.setMin("60.00");
		otcLimitBean2.setPay_way("1");
		list.add(otcLimitBean);
		list.add(otcLimitBean1);
		list.add(otcLimitBean2);
		return Result.buildSuccessResult(list );
	}
	
	/**
	 * <p>下游充值订单取消接口</p>
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NumberFormatException 
	 */
	@GetMapping("/merchant_buy_api/cancel")
	@ResponseBody
	public Result cancel(HttpServletRequest request,String orderId,String type) throws NumberFormatException, UnsupportedEncodingException {
		log.info(" 打开付款页面");
		log.info("订单号:"+orderId);
		DealOrder first = null;
		if(TYPE_QR.equals(type)) {
			first =  orderSerciceImpl.findOrderByOrderId(orderId);
		}else if(TYPE_CARD.equals(type)) {
			first = orderSerciceImpl.findOrderByOrderId(orderId);
		} else if (TYPE_CARD_RE.equals(type)) {//卡商取消充值   将充值订单置为失败
			Recharge rechargeOrder = rechargeServiceImpl.findOrderId(orderId);
			boolean flag = rechargeServiceImpl.updateStatusEr(rechargeOrder.getOrderId(), "卡商自己置订单为失败");
			if (flag) {
				return Result.buildSuccessResult();
			}
			return Result.buildFailResult("订单修改失败");
		}
		Result updataOrderStatusEr = cardBankOrderUtil.updataOrderEr(first.getOrderId(), HttpUtil.getClientIP(request));
		log.info("订单修改成功" + updataOrderStatusEr.toString());
		if (!updataOrderStatusEr.isSuccess()) {
			return Result.buildFailResult("订单修改失败");
		}
		return Result.buildSuccessResult();
	}
	/**
	 * <p>下游确认充值接口</p>
	 * <p>确认付款</p>
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NumberFormatException 
	 */
	@GetMapping("/merchant_buy_api/confirm_pay")
	@ResponseBody
	@Transactional
	public Result confirm_pay(String orderId,HttpServletRequest request,String type) throws NumberFormatException, UnsupportedEncodingException {
		log.info("【点击确认按钮】");
		log.info(orderId);
		if (TYPE_CARD_RE.equals(type)) {
			return Result.buildSuccessResult();
		}
		DealOrder findOrderByOrderId = orderSerciceImpl.findOrderByOrderId(orderId);
		String userId = "";
		if (TYPE_QR.equals(type)) {
			userId = findOrderByOrderId.getOrderAccount();
		} else {
			userId = findOrderByOrderId.getOrderQrUser();
		}
		Result enterOrderSu = enterOrderUtil.EnterOrderSu(orderId, userId, HttpUtil.getClientIP(request));
		log.info("【确认后返回值：" + enterOrderSu.toString() + "】");
		return enterOrderSu;
	}
	long dataSu(Date  timeBe , Date timeEnd) throws ParseException{
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time_1= dfs.format(new Date());
		String time_2 = dfs.format(timeEnd);
		Date begin=dfs.parse(time_1);
		Date end = dfs.parse(time_2);
		long miao=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒 
		return miao;
	}
	
	/**
	 * <p>码商确认提现订单到账</p>
	 * @param orderId		提现订单号  关联 交易订单号
	 * @param request		
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NumberFormatException 
	 */
	@PostMapping("/enterOrderSu")
	@ResponseBody
	@Transactional
	public Result enterOrderSu(String orderId,HttpServletRequest request,String userId) throws NumberFormatException, UnsupportedEncodingException {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(userId)) {
			return Result.buildFailResult("参数为空");
		}
		logUtil.addLog(request, "下游提现码商确认已收到提现转账,提现订单号:" + orderId + ",提现码商账号", userId);
		DealOrder order = orderSerciceImpl.findOrderByOrderId(orderId);
		Result enterOrderSu = enterOrderUtil.EnterOrderSu(order.getOrderId(), userId, HttpUtil.getClientIP(request));
		return enterOrderSu;
	}
}
