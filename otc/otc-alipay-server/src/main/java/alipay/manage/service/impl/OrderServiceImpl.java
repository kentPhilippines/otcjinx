package alipay.manage.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import alipay.manage.bean.*;
import alipay.manage.mapper.DealOrderMapper;
import alipay.manage.mapper.RechargeMapper;
import alipay.manage.mapper.RunOrderMapper;
import alipay.manage.mapper.WithdrawMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.service.OrderService;
@Component
public class OrderServiceImpl implements OrderService{
    @Autowired
	DealOrderMapper dealOrderMapper;
    @Autowired
	RunOrderMapper runOrderMapper;
    @Autowired
	RechargeMapper rechargeMapper;
    @Autowired
	WithdrawMapper withdrawMapper;

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

	@Override
	public List<DealOrder> findMyOrder(DealOrder order) {
		return dealOrderMapper.findMyOrder(order);
	}
}
