package alipay.manage.service.impl;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.mapper.DealOrderAppMapper;
import alipay.manage.service.OrderAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class orderAppServiceImpl implements OrderAppService {
	@Autowired DealOrderAppMapper dealOrderAppDao;
    @Override
    public boolean add(DealOrderApp dealApp) {
    	int insertSelective = dealOrderAppDao.insertSelective(dealApp);
        return insertSelective >0 && insertSelective < 2;
    }

	@Override
	public DealOrderApp findOrderByOrderId(String orderId) {
		
		return dealOrderAppDao.findOrderByOrderId(orderId);
	}

	@Override
	public DealOrderApp findAssOrder(String orderId) {
		return null;
	}

	@Override
	public void updateOrderEr(String orderId, String msg) {
		dealOrderAppDao.updateOrderEr(orderId,msg);
	}
}
