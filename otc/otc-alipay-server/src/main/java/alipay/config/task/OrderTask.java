package alipay.config.task;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.RunOrder;
import alipay.manage.mapper.DealOrderMapper;
import alipay.manage.service.RunOrderService;
import alipay.manage.util.OrderUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class OrderTask {
	/**
	 * <p>定时任务修改订单状态</p>
	 */
	private static final Log log = LogFactory.get();
	@Autowired
	RunOrderService RunOrderServiceimpl;
	@Resource
	private DealOrderMapper dealOrderDao;
	@Autowired
	private OrderUtil orderUtilImpl;

	/**
	 * 每十秒结算一次
	 */
	public void orderTask() {
		List<DealOrder> orderList = dealOrderDao.findSuccessAndNotAmount();
		for (DealOrder order : orderList) {
			try {
				List<RunOrder> runOrderList = RunOrderServiceimpl.findAssOrder(order.getOrderId());
				List<RunOrder> runOrderList1 = RunOrderServiceimpl.findAssOrder(order.getAssociatedId());
				if (CollUtil.isNotEmpty(runOrderList) || CollUtil.isNotEmpty(runOrderList1)) {
					dealOrderDao.updateSuccessAndAmount(order.getOrderId());
					continue;
				}
				orderUtilImpl.settlement(order);
			} catch (Exception e) {
				log.info("【异步结算发生异常：" + order.getOrderId() + "】");
			}
			ThreadUtil.execute(() -> {
				dealOrderDao.updateSuccessAndAmount(order.getOrderId());
			});
		}

	}


}
