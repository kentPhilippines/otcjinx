package alipay.manage.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import alipay.manage.bean.*;
import alipay.manage.mapper.DealOrderMapper;
import alipay.manage.mapper.RechargeMapper;
import alipay.manage.mapper.RunOrderMapper;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.util.IdGenerator;
import alipay.manage.util.SettingFile;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.service.OrderService;
import org.springframework.stereotype.Service;

@Component
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
	DealOrderMapper dealOrderMapper;
    @Autowired
	RunOrderMapper runOrderMapper;
    @Autowired
	RechargeMapper rechargeMapper;
    @Autowired
	WithdrawMapper withdrawMapper;
	@Autowired
	SettingFile settingFile;
    Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);
    
	@Override
	public List<DealOrder> findOrderByUser(String userId, String createTime) {
		List<DealOrder> selectByExample = dealOrderMapper.selectByExampleByMyId(userId,createTime);
		return selectByExample;
	}
	@Override
	public DealOrder getOrderByAssociatedId(String orderId) {
		return null;
	}

	@Override
	public List<RunOrder> findOrderRunByPage(RunOrder order) {
		RunOrderExample example = new RunOrderExample();
		RunOrderExample.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(order.getOrderAccount()))
			criteria.andOrderAccountEqualTo(order.getOrderAccount());
		if(StrUtil.isNotBlank(order.getTime())) {
			Date date = getDate(order.getTime());
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			System.out.println("开始时间："+calendar.getTime());
			Date time = calendar.getTime();
			calendar.set(Calendar.HOUR,23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND,59);
			calendar.set(Calendar.MILLISECOND,999);
			System.out.println("结束时间："+calendar.getTime());
			criteria.andCreateTimeBetween(time, calendar.getTime());
		}
		if(CollUtil.isNotEmpty(order.getOrderAccountList()))
			criteria.andOrderAccountListEqualTo(order.getOrderAccountList());
		if(StrUtil.isNotBlank(order.getRunType()))
			criteria.andRunTypeEqualTo(order.getRunType());
		example.setOrderByClause("createTime desc");
		return runOrderMapper.selectByExample(example);
	}
	Date getDate(String time){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = null;
		try {
			dateTime = simpleDateFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}
	@Override
	public List<DealOrder> findOrderByPage(DealOrder order) {
		return null;
	}

	@Override
	public List<Recharge> findRechargeOrder(Recharge bean) {
		RechargeExample example = new RechargeExample();
		RechargeExample.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(bean.getUserId()))
			criteria.andChargeBankcardEqualTo(bean.getUserId());
//		if(StrUtil.isNotBlank(bean.getTime())) {
//			Date date = getDate(bean.getTime());
//			Calendar calendar = new GregorianCalendar();
//			calendar.setTime(date);
//			calendar.set(Calendar.HOUR,0);
//			calendar.set(Calendar.MINUTE,0);
//			calendar.set(Calendar.SECOND,0);
//			calendar.set(Calendar.MILLISECOND,0);
//			System.out.println("开始时间："+calendar.getTime());
//			Date time = calendar.getTime();
//			calendar.set(Calendar.HOUR,23);
//			calendar.set(Calendar.MINUTE,59);
//			calendar.set(Calendar.SECOND,59);
//			calendar.set(Calendar.MILLISECOND,999);
//			System.out.println("结束时间："+calendar.getTime());
//			criteria.andCreateTimeBetween(time, calendar.getTime());
//		}
//		example.setOrderByClause("createTime desc");
		List<Recharge> selectByExample = rechargeMapper.selectByExample(example);
		return selectByExample;
	}

	@Override
	public List<Withdraw> findWithdrawOrder(Withdraw bean) {
		WithdrawExample example = new WithdrawExample();
	    WithdrawExample.Criteria criteria = example.createCriteria();
//		if(StrUtil.isNotBlank(bean.getUserId()))
//			criteria.andQrUserIdEqualTo(bean.getUserId());
		if(StrUtil.isNotBlank(bean.getTime())) {
			Date date = getDate(bean.getTime());
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			System.out.println("开始时间："+calendar.getTime());
			Date time = calendar.getTime();
			calendar.set(Calendar.HOUR,23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND,59);
			calendar.set(Calendar.MILLISECOND,999);
			System.out.println("结束时间："+calendar.getTime());
			criteria.andCreateTimeBetween(time, calendar.getTime());
		}
		example.setOrderByClause("createTime desc");
		List<Withdraw> selectByExample = withdrawMapper.selectByExample(example);
		return selectByExample;
	}

	@Override
	public DealOrder findOrderByOrderId(String orderId) {
		return null;
	}
	@Override
	public boolean updateOrderStatus(String orderId, String status, String mag) {
		return false;
	}

	/**
	 * <p>根据用户id查询自己的交易订单号记录</p>
	 * @param order
	 * @return
	 */
	@Override
	public List<DealOrder> findMyOrder(DealOrder order) {
		System.out.println("order  ->" +order.getOrderQrUser());
		return dealOrderMapper.findMyOrder(order);
	}
	/**
	 * 创建充值订单
	 * @param param
	 */
	@Override
	public Map<String, Object> createRechangeOrder(Map<String, String> param) {
		Map<String,Object> result = new HashMap<String,Object>();
//		String amount = param.get("amount");
//		String userId = param.get("qrUserId");
//		String depositor = param.get("depositor");//充值人姓名
//		String isTripartite = param.get("isTripartite");
//		String mobile = param.get("mobile");
//		if(StringUtils.isBlank(isTripartite)) {
//			isTripartite="1";//1存在三方收款渠道；2不存在三方收款渠道 这里默认为1
//		}
//		String qrRechargeType = param.get("qrRechargeType");//充值类型
//		String chargeReason = param.get("chargeReason");//充值说明
//		String orderId = IdGenerator.INSTANCE.orderSnoCreate();//订单id
//		log.info("码商充值订单号为："+orderId);
//		Recharge pqr = new Recharge();
//		pqr.setOrderId(orderId);
//		pqr.setUserId(userId);
//		pqr.setRechargeType(qrRechargeType);
//		pqr.setActualAmount( new BigDecimal(amount));
//		pqr.setAmount( new BigDecimal(amount));
//		pqr.setFee( new BigDecimal(0));
//		pqr.setIsTripartite(isTripartite);
//		pqr.setChargeReason(chargeReason);
//		pqr.setDepositor(depositor);
//		pqr.setRetain1(mobile);
//		pqr.setOrderStatus("0");
//		pqr.setOrderType("1"); 	// '充值类型:1码商充值2卡商充值',
//		pqr.setRetain1(settingFile.getName(settingFile.BANKCARD_PAYNOTFIYURL));
//		boolean flag = productQrcodeRechargeSvc.addProductQrcodeRecharge(pqr);
//		if(flag) {
//			log.info(orderId+"生成预订单成功,状态为:"+flag);
//			log.info(orderId+"上送渠道start....");
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("orderId",orderId);
//			map.put("amount",amount);
//			Map<String, Object> resultMap = service.rechange(map);
//			String code= resultMap.get("code")+"";
//			if ("1".equals(code)) {//渠道收单成功
//				log.info(orderId+"渠道收单成功，准备更新本地订单状态..");
//				Recharge p = new Recharge();
//				p.setOrderId(orderId);
//				p.setOrderStatus("1");
//				productQrcodeRechargeSvc.updateByOrderId(p);
//				result.put("success",true);
//				result.put("result",resultMap.get("payurl"));
//				return result;
//			}
//			else if("3".equals(code)){//渠道收单失败
//				log.info("渠道返回码：" + (String)resultMap.get("respcode"));
//				log.info(orderId+"渠道收单失败，准备更新本地订单状态");
//				Recharge p = new Recharge();
//				p.setOrderId(orderId);
//				p.setOrderStatus("3");
//				productQrcodeRechargeSvc.updateByOrderId(p);
//				result.put("success",false);
//				result.put("result","");
//				return result;
//			}
//		}
//		else {
//			log.info(orderId+"生成预订单失败,状态为:"+flag);
//			result.put("success",false);
//			result.put("result","");
//			return result;
//		}
		return result;
	}


	@Override
	public boolean addOrder(DealOrder orderApp) {
		int insertSelective = dealOrderMapper.insertSelective(orderApp);
		return insertSelective > 0 && insertSelective < 2;
	}
}
