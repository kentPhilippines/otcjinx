package deal.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.RechargeExample;
import deal.manage.bean.RechargeExample.Criteria;
import deal.manage.mapper.RechargeMapper;
import deal.manage.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Recharge;

import java.util.List;
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
		if (StrUtil.isNotBlank(bean.getOrderId())) {
			criteria.andOrderIdEqualTo(bean.getOrderId());
		}
		if (StrUtil.isNotBlank(bean.getUserId())) {
			criteria.andUserIdEqualTo(bean.getUserId());
		}
		return rechargeDao.selectByExample(example);
	}

	@Override
	public boolean updateStatusEr(String orderId, String message) {
		int a = rechargeDao.updateStatusEr(orderId,message);
		return a > 0 && a < 2;
	}

	@Override
	public boolean updateStatusSu(String orderId,String msg) {
		int a = rechargeDao.updateStatusSu(orderId , msg);
		return a > 0 && a < 2;
	}

}
