package deal.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.RechargeExample;
import deal.manage.bean.RechargeExample.Criteria;
import deal.manage.mapper.RechargeMapper;
import deal.manage.service.RechargeService;
import otc.bean.dealpay.Recharge;
@Component
public class RechargeServiceImpl implements RechargeService {
	@Autowired RechargeMapper rechargeDao;
	@Override
	public Recharge findOrderId(String orderId) {
		return rechargeDao.findOrderId(orderId);
	}

	@Override
	public boolean addOrder(Recharge order) {
		int insertSelective = rechargeDao.insertSelective(order);
		return insertSelective > 0  && insertSelective < 2;
	}

	@Override
	public List<Recharge> findRechargeOrder(Recharge bean) {
		RechargeExample example = new RechargeExample();
		Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(bean.getOrderId()))
			criteria.andOrderIdEqualTo(bean.getOrderId());
		if(StrUtil.isNotBlank(bean.getUserId()))
			criteria.andUserIdEqualTo(bean.getUserId());
		return rechargeDao.selectByExample(example);
	}

}
