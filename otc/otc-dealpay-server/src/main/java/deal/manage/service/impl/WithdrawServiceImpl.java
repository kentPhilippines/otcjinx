package deal.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import deal.manage.bean.Withdraw;
import deal.manage.bean.WithdrawExample;
import deal.manage.bean.WithdrawExample.Criteria;
import deal.manage.mapper.WithdrawMapper;
import deal.manage.service.WithdrawService;
@Component
public class WithdrawServiceImpl implements WithdrawService {
	@Autowired WithdrawMapper withdrawDao;
	@Override
	public boolean addOrder(Withdraw witb) {
		int insertSelective = withdrawDao.insertSelective(witb);
		return insertSelective > 0 && insertSelective < 2;
	}

	@Override
	public Withdraw findOrderId(String orderId) {
		return withdrawDao.findOrderId(orderId);
	}

	@Override
	public List<Withdraw> findWithdrawOrder(Withdraw bean) {
		WithdrawExample example = new WithdrawExample();
		Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(bean.getOrderId()))
			criteria.andOrderIdEqualTo(bean.getOrderId());
		if(StrUtil.isNotBlank(bean.getUserId()))
			criteria.andUserIdEqualTo(bean.getUserId());
		return withdrawDao.selectByExample(example);
	}

	@Override
	public boolean updateStatusEr(String orderId, String message) {
		int a = withdrawDao.updateStatusEr(orderId,message);
		return a<2 && a > 0;
	}

}
