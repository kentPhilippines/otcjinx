package alipay.config.task;

import org.springframework.beans.factory.annotation.Autowired;

import alipay.manage.mapper.DealOrderMapper;

public class OrderTask {
	@Autowired DealOrderMapper dealOrderDao;
	
	/**
	 * <p>定时任务修改订单状态</p>
	 */
	public void orderTask() {
		
	//	dealOrderDao.updateOrderTime();
		
	}
	
	
	
	
	

}
