package alipay.manage.contorller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.Recharge;
import alipay.manage.bean.RunOrder;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.Withdraw;
import alipay.manage.bean.util.PageResult;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.LogUtil;
import alipay.manage.util.OrderUtil;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.result.Result;

@Controller
@RequestMapping("/order")
public class OrderContorller {
	Logger log = LoggerFactory.getLogger(OrderContorller.class);
	@Autowired
	SessionUtil sessionUtil;
	@Autowired
	OrderService orderServiceImpl;
	@Autowired
	OrderUtil orderUtil;
	@Autowired
	LogUtil logUtil;
	@Autowired
	UserInfoService accountServiceImpl;
	
	private Lock lock = new ReentrantLock();
	/**
	 * <p>获取当前我，正在交易的订单</p>
	 * <p>若查询量过大，这里可能会造成服务器宕机</p>
	 * <p>查询量查询:  最大查询量为 ：  当前 可接单人数  如 当前可接单人数为  100 人 则可能造成  100个线程并发查询</p>
	 * @return
	 */
	@GetMapping("/findMyWaitReceivingOrder")
	@ResponseBody
	public Result findMyWaitReceivingOrder(HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		List<DealOrder> list = new ArrayList();
		/**
		 * <p>接口请求参数</p>
		 * 		accountId  : 码商 账户id
		 * <p>成功返回参数</p>
		 * JsonResult :
		 * 			code : 200
		 * 			success : true
		 * 			result : List<QrcodeDealOrder>
		 * 				<p>查询成功必传参数</p>
		 * 						orderId : 订单号
		 * 						dealAmount : 订单交易金额
		 * 						createTime : 订单创建时间
		 * 						dealType : 订单类型  (中文 ced:UTF-8)   例  alipay_qr:支付宝扫码
		 */					 
		return null;
	}
	@GetMapping("/findMyWaitConfirmOrder")
	@ResponseBody
	public Result findMyWaitConfirmOrder(HttpServletRequest request,String pageNum,String pageSize,String createTime) {
		UserInfo user2 = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user2)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
		List<DealOrder> listOrder = orderServiceImpl.findOrderByUser(user2.getUserId(),createTime);
		log.info("获取的集合:"+ listOrder);
		PageInfo<DealOrder> pageInfo = new PageInfo<DealOrder>(listOrder);
		PageResult<DealOrder> pageR = new PageResult<DealOrder>();
		pageR.setContent(pageInfo.getList());
		pageR.setPageNum(pageInfo.getPageNum());
		pageR.setTotal(pageInfo.getTotal());
		pageR.setTotalPage(pageInfo.getPages());
		return Result.buildSuccessResult(pageR);
	}

	@GetMapping("/userConfirmToPaid")
	@ResponseBody
	@Transactional
	public Result userConfirmToPaid(HttpServletRequest request,String orderId) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		log.info("订单号收到款项,状态变更为成功："+orderId);
		log.info("【码商手动置订单为成功："+user.getUserId()+"，订单号："+orderId+"】");
		DealOrder order = orderServiceImpl.getOrderByAssociatedId(orderId);
		Date createTime = order.getCreateTime();
		String msg = "码商手动置交易订单为成功，当前交易订单为："+order.getOrderId()+"，当前订单金额为："+order.getDealAmount();
		logUtil.addLog(request, msg, user.getUserId());
		boolean expired = DateUtil.isExpired(createTime,DateField.SECOND,1200,new Date());
		if(!expired)
			return Result.buildFailResult("无权限，请联系客服人员操作");
		Result updataOrder = orderUtil.updataOrder(orderId,Common.User.RUN_TYPE_ARTIFICIAL);//成功状态，人工操作
		if(updataOrder.isSuccess()) {
			return updataOrder;
		}
		return Result.buildFailResult("无权限，请联系客服人员操作");
	}
	/**
	 * <p>获取个人流水</p>
	 * @param request
	 * @param startTime
	 * @param pageNum
	 * @param pageSize
	 * @param accountChangeTypeCode
	 * @return
	 */
	@GetMapping("/findMyAccountChangeLogByPage")
	@ResponseBody
	@Transactional
	public Result findMyAccountChangeLogByPage(HttpServletRequest request,String startTime,
			String pageNum,String pageSize,String accountChangeTypeCode) {
		RunOrder order = new RunOrder();
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		order.setOrderAccount(user.getUserId());
		if(StrUtil.isNotBlank(startTime)) 
			order.setTime(startTime);
		if(StrUtil.isNotBlank(accountChangeTypeCode))
			order.setRunType(accountChangeTypeCode);
		PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
		List<RunOrder> orderList = orderServiceImpl.findOrderRunByPage(order);
		PageInfo<RunOrder> pageInfo = new PageInfo<RunOrder>(orderList);
		PageResult<RunOrder> pageR = new PageResult<RunOrder>();
		pageR.setContent(pageInfo.getList());
		pageR.setPageNum(pageInfo.getPageNum());
		pageR.setTotal(pageInfo.getTotal());
		pageR.setTotalPage(pageInfo.getPages());
		return Result.buildSuccessResult(pageR);
	}
	@GetMapping("/findLowerLevelAccountChangeLogByPage")
	@ResponseBody
	@Transactional
	public Result findLowerLevelAccountChangeLogByPage(
			HttpServletRequest request,
			String startTime,
			String pageNum,
			String pageSize,
			String accountChangeTypeCode,
			String userName
			) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		List<String> userList =  accountServiceImpl.findSunAccountByUserId(user.getUserId());	
		if(StrUtil.isNotBlank(userName)) {
			if(!userList.contains(userName)) 
				return Result.buildFailMessage("输入账号有误");
			userList.clear();
			userList.add(userName);
		} 
		userList.add(user.getUserId());
		log.info("子账户"+userList.toString());
		RunOrder orderRun = new RunOrder();
		orderRun.setOrderAccountList(userList);
		if(StrUtil.isNotBlank(startTime))
			orderRun.setTime(startTime);
		if(StrUtil.isNotBlank(accountChangeTypeCode))
			orderRun.setRunType(accountChangeTypeCode);
		PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
		List<RunOrder> orderList = orderServiceImpl.findOrderRunByPage(orderRun);
		PageInfo<RunOrder> pageInfo = new PageInfo<RunOrder>(orderList);
		PageResult<RunOrder> pageR = new PageResult<RunOrder>();
		pageR.setContent(pageInfo.getList());
		pageR.setPageNum(pageInfo.getPageNum());
		pageR.setTotal(pageInfo.getTotal());
		pageR.setTotalPage(pageInfo.getPages());
		return Result.buildSuccessResult(pageR);
	}
	@GetMapping("/findLowerLevelAccountReceiveOrderRecordByPage")
	@ResponseBody
	@Transactional
	public Result findLowerLevelAccountReceiveOrderRecordByPage(
			HttpServletRequest request,
			String startTime,
			String pageNum,
			String pageSize,
			String accountChangeTypeCode,
			String userName,
			String orderState
			) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		List<String> userList =  accountServiceImpl.findSunAccountByUserId(user.getUserId());	
		if(StrUtil.isNotBlank(userName)) {
			if(!userList.contains(userName)) 
				return Result.buildFailMessage("输入账号有误");
			userList.clear();
			userList.add(userName);
		} 
		userList.add(user.getUserId());
		log.info("子账户"+userList.toString());
		DealOrder order = new DealOrder();
		if(StrUtil.isNotBlank(startTime)) 
			order.setTime(startTime);
		if(StrUtil.isNotBlank(orderState))
			order.setOrderStatus( orderState);
		order.setOrderQrUserList(userList); 
		PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
		List<DealOrder> orderList = orderServiceImpl.findOrderByPage(order);
		PageInfo<DealOrder> pageInfo = new PageInfo<DealOrder>(orderList);
		PageResult<DealOrder> pageR = new PageResult<DealOrder>();
		pageR.setContent(pageInfo.getList());
		pageR.setPageNum(pageInfo.getPageNum());
		pageR.setTotal(pageInfo.getTotal());
		pageR.setTotalPage(pageInfo.getPages());
		return Result.buildSuccessResult(pageR);
	}
	/**
	 * <p>获取个人流水</p>
	 * @param request
	 * @param startTime
	 * @param pageNum
	 * @param pageSize
	 * @param accountChangeTypeCode
	 * @return
	 */
	@GetMapping("/findMyRechargeWithdrawLogByPage")
	@ResponseBody
	public Result findMyRechargeWithdrawLogByPage(HttpServletRequest request,
			String startTime,
			String pageNum,
			String pageSize,
			String orderType
			) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		if(StrUtil.isBlank(orderType))
			orderType = "1";
		if(orderType.equals("1")) {//充值
			Recharge bean = new Recharge();
			bean.setUserId(user.getUserId());
			if(StrUtil.isNotBlank(startTime))
				bean.setTime(startTime);
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<Recharge> witList = orderServiceImpl.findRechargeOrder(bean);
			PageInfo<Recharge> pageInfo = new PageInfo<Recharge>(witList);
			PageResult<Recharge> pageR = new PageResult<Recharge>();
			pageR.setContent(pageInfo.getList());
			pageR.setPageNum(pageInfo.getPageNum());
			pageR.setTotal(pageInfo.getTotal());
			pageR.setTotalPage(pageInfo.getPages());
			return Result.buildSuccessResult(pageR);
		}else {//提现
			Withdraw bean = new Withdraw();
			bean.setUserId(user.getUserId());
			if(StrUtil.isNotBlank(startTime))
				bean.setTime(startTime);
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<Withdraw> witList = orderServiceImpl.findWithdrawOrder(bean);
			PageInfo<Withdraw> pageInfo = new PageInfo<Withdraw>(witList);
			PageResult<Withdraw> pageR = new PageResult<Withdraw>();
			pageR.setContent(pageInfo.getList());
			pageR.setPageNum(pageInfo.getPageNum());
			pageR.setTotal(pageInfo.getTotal());
			pageR.setTotalPage(pageInfo.getPages());
			return Result.buildSuccessResult(pageR);
		}
	}
	/**
	 * <p>码商确认提现成功</p>
	 * @param orderId			提现订单号
	 * @return

	@GetMapping("/enterOrderSu")
	@ResponseBody
	public Result enterOrderSu(String orderId,HttpServletRequest request) {
		UserInfo user = sessionUtil.getUser(request);
		if (ObjectUtil.isNull(user)) {
	        log.info("当前用户未登陆");
	        return Result.buildFailMessage("当前用户未登陆");
	    }
		if(StrUtil.isBlank(orderId)) 
			 return Result.buildFailResult("参数为空");
		Map<String,Object> paramMap = new HashMap();
		paramMap.put("orderId", orderId);
		paramMap.put("userId", user.getUserId());
		String URL = settingFile.getName(SettingFile.ENTER_ORDER_SU);
		log.info("码商确认url："+URL);
		String post = HttpUtil.post(URL, paramMap);
		log.info("码商收款确认返回：："+post);
		JSONObject parseObj = JSONUtil.parseObj(post);
		JsonResult bean = JSONUtil.toBean(parseObj, JsonResult.class);
		return bean;
	}
		 */
}