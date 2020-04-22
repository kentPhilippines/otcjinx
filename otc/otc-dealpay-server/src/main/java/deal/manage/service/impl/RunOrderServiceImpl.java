package deal.manage.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
		if(ObjectUtil.isNotNull(order.getRunOrderType()))
			criteria.andRunOrderTypeEqualTo(order.getRunOrderType());
		if(ObjectUtil.isNotNull(order.getOrderAccountList())) 
			criteria.andOrderAccountIn(order.getOrderAccountList());
		if(StrUtil.isNotBlank(order.getTime())) {
			Date date = getDate(order.getTime());
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			System.out.println("开始时间："+calendar.getTime());
			Date time = calendar.getTime();
			calendar.set(Calendar.HOUR,23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND,59);
			calendar.set(Calendar.MILLISECOND,999);
			System.out.println("结束时间："+calendar.getTime());
			criteria.andCreateTimeBetween(time, calendar.getTime());
		}
		example.setOrderByClause("createTime desc");
		return runorderDao.selectByExample(example);
	}
	
	
	
	
	Date getDate(String time){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = null;
		try {
			dateTime = simpleDateFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}

}
