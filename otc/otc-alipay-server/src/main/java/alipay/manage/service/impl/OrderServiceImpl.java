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
import otc.util.number.Number;

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
    @Autowired DealOrderMapper dealOrderMapper;
    @Autowired RunOrderMapper runOrderMapper;
    @Autowired RechargeMapper rechargeDao;
    @Autowired WithdrawMapper withdrawMapper;
	@Autowired SettingFile settingFile;
    Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);
    
	@Override
	public List<DealOrder> findOrderByUser(String userId, String createTime) {
		List<DealOrder> selectByExample = dealOrderMapper.selectByExampleByMyId(userId,createTime);
		return selectByExample;
	}
	@Override
	public DealOrder getOrderByAssociatedId(String orderId) {
		DealOrder  order = dealOrderMapper.findOrderByAssociatedId(orderId);
		return order;
	}

	@Override
	public List<RunOrder> findOrderRunByPage(RunOrder order) {
		log.info("获取订单账户 " + order.getOrderAccount());
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
		DealOrderExample example=new DealOrderExample();
		DealOrderExample.Criteria criteria = example.createCriteria();
		if (StrUtil.isNotBlank(order.getOrderQrUser()))
			criteria.andOrderQrUserEqualTo(order.getOrderQrUser());
		if (CollUtil.isNotEmpty(order.getOrderQrUserList()))
			criteria.andOrderQrUserListEqualTo(order.getOrderQrUserList());
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
		if(StrUtil.isNotBlank(order.getAssociatedId()))
			criteria.andAssociatedIdEqualTo(order.getAssociatedId());
		if(StrUtil.isNotBlank(order.getOrderAccount()))
			criteria.andOrderAccountEqualTo(order.getOrderAccount());
		if(StrUtil.isNotBlank(order.getOrderStatus()))
			criteria.andOrderStatusEqualTo(order.getOrderStatus());
		if(StrUtil.isNotBlank(order.getOrderId()))
			criteria.andOrderIdEqualTo(order.getOrderId());
		example.setOrderByClause("createTime desc");
		return dealOrderMapper.selectByExample(example);
	}

	@Override
	public List<Recharge> findRechargeOrder(Recharge bean) {
		RechargeExample example = new RechargeExample();
		RechargeExample.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(bean.getUserId()))
			criteria.andChargeBankcardEqualTo(bean.getUserId());
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
		List<Recharge> selectByExample = rechargeDao.selectByExample(example);
		return selectByExample;
	}

	@Override
	public List<Withdraw> findWithdrawOrder(Withdraw bean) {
		WithdrawExample example = new WithdrawExample();
	    WithdrawExample.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(bean.getUserId()))
			criteria.andAccnameEqualTo(bean.getUserId());
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
		return dealOrderMapper.findOrderByOrderId(orderId);
	}
	@Override
	public boolean updateOrderStatus(String orderId, String status, String mag) {
		int a = dealOrderMapper.updateOrderStatus(orderId,status,mag);
		return a > 0 && a < 2; 
	}

	/**
	 * <p>根据用户id查询自己的交易订单号记录</p>
	 * @param order
	 * @return
	 */
	@Override
	public List<DealOrder> findMyOrder(DealOrder order) {
		return dealOrderMapper.findMyOrder(order);
	}
	@Override
	public boolean addOrder(DealOrder orderApp) {
		orderApp.setOrderId(Number.getOrderQr());
		int insertSelective = dealOrderMapper.insertSelective(orderApp);
		return insertSelective > 0 && insertSelective < 2;
	}

	@Override
	public boolean updataOrderStatusByOrderId(String orderId, String status) {
         log.info("=======【根据订单编号修改交易订单为成功,捕获订单编号:"+orderId+"】======");
         DealOrder record=new DealOrder();
         DealOrderExample example=new DealOrderExample();
         DealOrderExample.Criteria criteriaDealOrder=example.createCriteria();
         criteriaDealOrder.andOrderIdEqualTo(orderId);
         record.setOrderStatus(status);
         record.setCreateTime(null);
		int updateByExampleSelective = dealOrderMapper.updateByExampleSelective(record, example);
        boolean flag=updateByExampleSelective>0 && updateByExampleSelective<2;
		return flag;
	}

	@Override
	public boolean updataOrderisNotifyByOrderId(String orderId, String isNotify) {
		log.info("=======【根据订单编号修改交易订单是否发送通知为YES,捕获订单编号:"+orderId+"】=======");
		DealOrder record=new DealOrder();
		DealOrderExample example=new DealOrderExample();
		DealOrderExample.Criteria criteriaDealOrder=example.createCriteria();
		criteriaDealOrder.andOrderIdEqualTo(orderId);
		record.setIsNotify(isNotify);
		record.setCreateTime(null);
		int updateByExampleSelective = dealOrderMapper.updateByExampleSelective(record, example);
		boolean flag=updateByExampleSelective>0 && updateByExampleSelective<2;
		log.info("=======【修改订单通知状态完毕:修改结果为:"+flag+"】=======");
		return flag;
	}
	@Override
	public boolean addRechargeOrder(Recharge order) {
		int insertSelective = rechargeDao.insertSelective(order);
		return insertSelective > 0 && insertSelective < 2;
	}
}
