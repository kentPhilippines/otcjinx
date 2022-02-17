package alipay.manage.service.impl;

import alipay.manage.bean.DealOrderApp;
import alipay.manage.mapper.DealOrderAppMapper;
import alipay.manage.service.OrderAppService;
import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Component
public class orderAppServiceImpl implements OrderAppService {
	@Resource
	private DealOrderAppMapper dealOrderAppDao;

	@Override
	public boolean add(DealOrderApp dealApp) {
		int insertSelective = dealOrderAppDao.insertSelective(dealApp);
		return insertSelective > 0 && insertSelective < 2;
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

	@Override
	public void updateOrderSu(String orderId, String orderStatus) {
		dealOrderAppDao.updateOrderSu(orderId, orderStatus);
	}

	@Override
	public DealOrderApp findOrderByApp(String appId, String appOrderId) {
		List<DealOrderApp> orderByApp = dealOrderAppDao.findOrderByApp(appId, appOrderId);
		for ( DealOrderApp app  : orderByApp){
			if(app.getOrderStatus().toString().equals(Common.Order.DealOrder.ORDER_STATUS_SU.toString())){
				return app;
			}
		}
		if(CollUtil.isNotEmpty(orderByApp)){
			return CollUtil.getFirst(orderByApp);
		}
		return new DealOrderApp();
	}

	@Override
	public DealOrderApp findOrderByAppSum(String appId) {
		DealOrderApp orderByAppSum = dealOrderAppDao.findOrderByAppSum(appId);
		if (null == orderByAppSum) {
			orderByAppSum = new DealOrderApp();
			orderByAppSum.setOrderAmount(new BigDecimal("0"));
		}

		return orderByAppSum;
	}

}
