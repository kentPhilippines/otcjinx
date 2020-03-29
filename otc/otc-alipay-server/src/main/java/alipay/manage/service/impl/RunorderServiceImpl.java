package alipay.manage.service.impl;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import alipay.manage.bean.RunOrder;
import alipay.manage.mapper.RunOrderMapper;
import alipay.manage.service.RunOrderService;
import otc.util.number.Number;
@Component
public class RunorderServiceImpl implements RunOrderService{
	@Autowired RunOrderMapper runOrderDao;
	@Override
	public boolean addOrder(RunOrder run) {
		run.setOrderId(Number.getRunOrderId());
		int integer = runOrderDao.insertSelective(run);
		return integer > 0 && integer < 2;
	}
}
