package alipay.manage.service.impl;

import alipay.manage.bean.RunOrder;
import alipay.manage.mapper.RunOrderMapper;
import alipay.manage.service.RunOrderService;
import org.springframework.stereotype.Component;
import otc.util.number.Number;

import javax.annotation.Resource;

@Component
public class RunorderServiceImpl implements RunOrderService {
	@Resource
	RunOrderMapper runOrderDao;

	@Override
	public boolean addOrder(RunOrder run) {
		run.setOrderId(Number.getRunOrderId());
		int integer = runOrderDao.insertSelective(run);
		return integer > 0 && integer < 2;
	}
}
