package deal.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import deal.manage.bean.Runorder;
import deal.manage.bean.RunorderExample;
import deal.manage.bean.RunorderExample.Criteria;
import deal.manage.mapper.RunorderMapper;
import deal.manage.service.RunOrderService;
@Component
public class RunOrderServiceImpl implements RunOrderService {
	@Autowired RunorderMapper runorderDao;
	@Override
	public boolean addOrder(Runorder run) {
		int selective = runorderDao.insertSelective(run);
		return selective > 0 && selective < 2;
	}

	@Override
	public List<Runorder> findOrderRunByPage(Runorder order) {
		RunorderExample example = new RunorderExample();
		Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(order.getOrderAccount()))
			criteria.andOrderAccountEqualTo(order.getOrderAccount());
		if(StrUtil.isNotBlank(order.getRunType()))
			criteria.andRunTypeEqualTo(order.getRunType());
		if(StrUtil.isNotBlank(order.getOrderId()))
			criteria.andOrderIdEqualTo(order.getOrderId());
		if(ObjectUtil.isNotNull(order.getRunOrderType()))
			criteria.andRunOrderTypeEqualTo(order.getRunOrderType());
		return runorderDao.selectByExample(example);
	}

}
