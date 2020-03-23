package alipay.manage.service.impl;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.service.OrderAppService;
import org.springframework.stereotype.Component;

@Component
public class orderAppServiceImpl implements OrderAppService {
    @Override
    public boolean add(DealOrderApp dealApp) {
        return false;
    }

	@Override
	public DealOrderApp findOrderByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}
}
