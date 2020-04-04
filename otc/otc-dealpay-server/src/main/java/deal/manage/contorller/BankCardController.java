package deal.manage.contorller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import deal.manage.bean.BankList;
import deal.manage.bean.UserFund;
import deal.manage.bean.UserInfo;
import deal.manage.bean.Withdraw;
import deal.manage.service.BankListService;
import deal.manage.service.RechargeService;
import deal.manage.service.UserInfoService;
import deal.manage.service.WithdrawService;
import deal.manage.util.LogUtil;
import deal.manage.util.OrderUtil;
import deal.manage.util.SessionUtil;
import otc.api.alipay.Common;
import otc.result.Result;
import otc.util.encode.HashKit;
import otc.util.number.Number;
@Controller
@RequestMapping("/bankcard")
public class BankCardController {
	private static final Log log = LogFactory.get();
	@Autowired BankListService bankCardSvc;
	@Autowired RechargeService rechargeSvc;
	@Autowired UserInfoService userInfoServiceImpl;
	@Autowired WithdrawService withdrawServiceImpl;
	@Autowired OrderUtil orderUtil;
	@Autowired SessionUtil sessionUtil;
	@Autowired LogUtil logUtil;
	private static final String ACCNAME = "accname";
	private static final String BANK_NAME = "bankname";
	private static final String BANK_NO = "bankno";
	private static final String AMOUNT = "amount";
	private static final String USERID = "qruserId";
	private static final String MOBILE = "mobile";
	private static final String MONEY_PWD = "moneyPwd";
	/**
	 * 卡商提现接口
	 * @param request
	 * @return
	 */
	@PostMapping("/withdraw")
	@ResponseBody
	public Result bankcardwithdraw(
			Withdraw q,
			String withdrawAmount,
			String moneyPwd,
			String bankCard,
			String accountHolder,
			String mobile,
			String bankCardAccount,
			HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if(ObjectUtil.isNull(user)) 
			return Result.buildFailMessage("当前用户未登录");
		log.info("【用户提现，提现人："+user.getUserId()+"】");
		String orderId = Number.getWitOrder();
		BankList  banklist = bankCardSvc.findBankByNo(bankCardAccount);
		if(ObjectUtil.isNull(banklist)) 
			return Result.buildFailResult("银行卡信息不正确");
		Map<String, String> map = new HashMap();
		map.put(ACCNAME, banklist.getAccountHolder());
		map.put(BANK_NAME, bankCard);
		map.put(BANK_NO, bankCardAccount);
		map.put(AMOUNT, withdrawAmount);
		map.put(USERID, user.getUserId());
		map.put(MOBILE,mobile);
		map.put(MONEY_PWD,moneyPwd);
		Result clickWithdraw = isClickWithdraw(map);
		if(!clickWithdraw.isSuccess()) {
			return clickWithdraw;
		}
		String msg = "卡商发起提现操作,当前提现参数：开户名："+accountHolder+"，银行名称："+bankCard+
				"，关联卡商账号："+user.getUserId()+"，提现手机号："+ mobile+"，提现金额："+withdrawAmount+"，提现验证密码："+clickWithdraw.isSuccess();
		logUtil.addLog(request, msg, user.getUserId());
		log.info(orderId+"start卡商提现<<");
		Withdraw qw = new Withdraw();
		qw.setAccname(banklist.getAccountHolder());//开户名
		qw.setActualAmount(new BigDecimal(withdrawAmount));//实际提现金额
		qw.setAmount(new BigDecimal(withdrawAmount));//提现金额
		qw.setBankName(banklist.getOpenAccountBank());//银行名称
		qw.setBankNo(bankCardAccount);//银行卡号
		qw.setFee(new BigDecimal(0));
		qw.setOrderStatus(Common.Order.Wit.ORDER_STATUS_YU);
		qw.setUserId(user.getUserId());//提现码商
		qw.setMobile(mobile);//手机号
		qw.setWithdrawType(otc.api.dealpay.Common.Order.Wit.WIT_BK);
		log.info("卡商提现订单号为："+orderId);
		boolean flag = withdrawServiceImpl.addOrder(qw);
		log.info(orderId+"卡商提现，生成订单状态："+flag);
		if(!flag) 
			return Result.buildFailResult("生成提现订单失败");
		log.info(orderId+"生成流水----扣保证金");
		Result cardsubRunning = orderUtil.cardsubRunning(orderId);
		log.info(orderId+"卡商提现end>>");
		if(cardsubRunning.isSuccess())
			return Result.buildSuccessResult("提现支付订单获取成功", "");
		return cardsubRunning;
	}
	
	/**
	 * <p>验证钱是否够，密码是否正确</p>
	 * @param map
	 * @return
	 */
	Result isClickWithdraw(Map<String, String> map) {
		UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(map.get(USERID));
		String payPasword = userInfo.getPayPasword();
		Result password = HashKit.encodePassword(map.get(USERID), map.get(MONEY_PWD), userInfo.getSalt());
		if(password.isSuccess()) {
			if(!password.getResult().toString().equals(payPasword))
				return Result.buildFailMessage("资金密码错误");
		}
		UserFund userFund = userInfoServiceImpl.findUserFundByAccount(map.get(USERID));
		BigDecimal accountbalance = userFund.getAccountBalance();
		BigDecimal amount = new BigDecimal(map.get(AMOUNT));
		// TODO   获取当前卡商虚拟冻结金额
		if(accountbalance.compareTo(amount)== -1)
			return Result.buildFailResult("当前金额不足，请重新填写金额");
		return Result.buildSuccessResult();
	}
	
	/**
	 * 码商充值买币
	 * @param p
	 * @param request
	 * @return
	@PostMapping("/qrpay")
	@ResponseBody
	public String  qrpay(HttpServletRequest request) {
		String amount = request.getParameter("amount");//码商充值金额
		String orderId = request.getParameter("orderId");//码商充值单号
		String payurl = settingFile.getName(settingFile.PAYURL)+orderId+"&type=1";
		log.info(orderId+"码商充值匹配银行卡<<");
		//查询处理中的卡商代付订单
	//	List<QrcodeWithdraw> cardWithdrawOrder = qrcodeWithdrawSvc.getCardWithdrawOrder(amount);
		List<Withdraw> cardWithdrawOrder = new ArrayList<QrcodeWithdraw>();
		List<BankList> bankList = bankCardSvc.selectBankList(Double.parseDouble(amount));
		if(cardWithdrawOrder == null || cardWithdrawOrder.size() == 0) {
			log.info(orderId+"匹配银行卡码商上传银行卡:"+bankList);
//			JsonResult createBankOrder = cardBankOrderUtil.createBankOrder(bankList, orderId,false);
//			log.info(orderId+"createBankOrder执行结果"+createBankOrder.toString());
			if(bankList!=null && bankList.size()>0) {
				JsonResult createBankOrder = cardBankOrderUtil.createBankOrder(bankList, orderId,false);
				log.info(orderId+"createBankOrder执行结果"+createBankOrder.toString());
				if(createBankOrder.isSuccess()) {
					JSONObject json = new JSONObject();
					json.put("success", true);
					json.put("payurl", payurl);
					return json.toJSONString();
				}
				else {
					JSONObject json = new JSONObject();
					json.put("success", false);
					json.put("payurl", "");
					return json.toJSONString();
				}
			}
			else {
				JSONObject json = new JSONObject();
				json.put("success", false);
				json.put("payurl", "");
				return json.toJSONString();
			}
		}
		QrcodeWithdraw qrcodeWithdraw = cardWithdrawOrder.get(0);
		//查询状态为成功和处理中的码商充值订单表
		List<ProductQrcodeRecharge> rechangeOrder = productQrcodeRechargeSvc.getRechangeOrder(qrcodeWithdraw.getWithdrawOrderId());
		
		BigDecimal qrrecharge = new BigDecimal(amount);//码商充值金额
		BigDecimal qrrecharge_withdraw = new BigDecimal(0);//并联提现金额
		BigDecimal withdraw_amount = qrcodeWithdraw.getAmount();//提现订单金额
		for(ProductQrcodeRecharge p : rechangeOrder) {
			qrrecharge_withdraw = qrrecharge_withdraw.add(p.getAmount());
		}
		
		//码商提现与卡商充值刚好相等
		if((rechangeOrder == null | rechangeOrder.size() == 0) && withdraw_amount.compareTo(qrrecharge.add(qrrecharge_withdraw))==0) {
			BankCardQrList bank = new BankCardQrList();
			bank.setBankCardAccount(qrcodeWithdraw.getBankNo());
			bankList = bankCardSvc.findBankCardByBankInfo(bank);
			boolean updateByOrderId = updateDpayOrder(orderId, bankList, qrcodeWithdraw);
			if(updateByOrderId) {
				JsonResult createBankOrder = cardBankOrderUtil.createBankOrder(bankList, orderId,true);
				log.info(orderId+"createBankOrder执行结果"+createBankOrder.toString());
				JSONObject json = new JSONObject();
				json.put("success", true);
				json.put("payurl", payurl);
				return json.toJSONString();
			}
			else {
				JSONObject json = new JSONObject();
				json.put("success", false);
				json.put("payurl", "");
				return json.toJSONString();
			}
		}
		
		if(withdraw_amount.compareTo(qrrecharge.add(qrrecharge_withdraw))==1 || 
				withdraw_amount.compareTo(qrrecharge.add(qrrecharge_withdraw))==0) {
			BankCardQrList bank = new BankCardQrList();
			bank.setBankCardAccount(qrcodeWithdraw.getBankNo());
			bankList = bankCardSvc.findBankCardByBankInfo(bank);
			boolean updateByOrderId = createDpayorderAndUpdate(orderId, qrcodeWithdraw, qrrecharge);
			if(updateByOrderId) {
				JsonResult createBankOrder = cardBankOrderUtil.createBankOrder(bankList, orderId,true);
				log.info(orderId+"createBankOrder执行结果"+createBankOrder.toString());
				JSONObject json = new JSONObject();
				json.put("success", true);
				json.put("payurl", payurl);
				return json.toJSONString();
			}
			else {
				JSONObject json = new JSONObject();
				json.put("success", false);
				json.put("payurl", "");
				return json.toJSONString();
			}
			
		}
		JsonResult createBankOrder = cardBankOrderUtil.createBankOrder(bankList, orderId,true);
		log.info(orderId+"createBankOrder执行结果"+createBankOrder.toString());
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("payurl",payurl );
		return json.toJSONString();
		
	}
	
	
	
	
*/	

	/**
	 * 码商提现卖币
	 * @param p
	 * @param request
	 * @return
	
	@PostMapping("/qrwithdraw")
	@ResponseBody
	public String qrwithdraw(HttpServletRequest request) {
		String amount = request.getParameter("amount");//码商充值金额
		String orderId = request.getParameter("orderId");//码商充值单号
		String bankno = request.getParameter("bankno");
		log.info(orderId+"进入到码商提现接口");
		List<BankCardQrList> bankList = bankCardSvc.selectQrBankList(bankno);
		JsonResult createBankOrder = cardBankOrderUtil.createBankOrder(bankList, orderId);
		if(createBankOrder.isSuccess()) {
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("payurl", settingFile.getName(settingFile.PAYURL)+orderId);
			return json.toJSONString();
		}
		JSONObject json = new JSONObject();
		json.put("success", false);
		json.put("payurl","");
		log.info(orderId+"码商提现接口返回"+json.toJSONString());
		return json.toJSONString();
	}

	private boolean updateDpayOrder(String orderId, List<BankCardQrList> bankList, QrcodeWithdraw qrcodeWithdraw) {
		log.info(orderId+"匹配银行卡为提现卡商银行卡:"+bankList.toString());
		//更新卡商提现订单号到码商充值订单
		ProductQrcodeRecharge p = new ProductQrcodeRecharge();
		p.setOrderId(orderId);
		p.setRetain3(qrcodeWithdraw.getWithdrawOrderId());
		boolean updateByOrderId = productQrcodeRechargeSvc.updateByOrderId(p);
		log.info(orderId+"更新卡商提现订单号到码商充值订单结果:"+updateByOrderId);
		return updateByOrderId;
	}
/**
	private boolean createDpayorderAndUpdate(String orderId, QrcodeWithdraw qrcodeWithdraw, BigDecimal qrrecharge) {
		QrcodeWithdraw qw = new QrcodeWithdraw();
		qw.setAccname(qrcodeWithdraw.getAccname());//开户名
		qw.setActualAmount(qrrecharge);//实际提现金额
		qw.setAmount(qrrecharge);//提现金额
		qw.setBankName(qrcodeWithdraw.getBankName());//银行名称
		qw.setBankNo(qrcodeWithdraw.getBankNo());//银行卡号
		qw.setFee(new BigDecimal(0));
		qw.setOrderStatus(Constant.ORDER_INPROCESSING);
		qw.setQrUserId(qrcodeWithdraw.getQrUserId());//提现码商
		qw.setMobile(qrcodeWithdraw.getMobile());//手机号
		qw.setWithdrawOrderId("KD_"+orderId);
		qw.setWithdrawType("银行卡");
		qw.setStatus("0");
		qw.setOrderType(Constant.CARDRECHANGETYPE);
		qw.setCreateTime(new Date());
		qw.setRetain2(qrcodeWithdraw.getWithdrawOrderId());//并联卡商主代付订单
		log.info("码卡商提现子订单号为："+"KD_"+orderId+",对应码商充值订单"+orderId+",对应卡商提现订单："+qrcodeWithdraw.getWithdrawOrderId());
		boolean flag = qrcodeWithdrawSvc.addQrcodeWithdraw(qw);
		boolean updateByOrderId = false;
		if(flag) {
			ProductQrcodeRecharge p = new ProductQrcodeRecharge();
			p.setOrderId(orderId);
			p.setRetain3("KD_"+orderId);
			updateByOrderId = productQrcodeRechargeSvc.updateByOrderId(p);
			log.info(orderId+"更新卡商提现订单号到码商充值订单结果:"+updateByOrderId);
		}
		return updateByOrderId;
	}
	*/
	
}
